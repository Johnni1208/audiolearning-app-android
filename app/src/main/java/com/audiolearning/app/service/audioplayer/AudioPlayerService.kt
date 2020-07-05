package com.audiolearning.app.service.audioplayer

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.audiolearning.app.R
import com.audiolearning.app.notification.AudioNotificationManager
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import timber.log.Timber

private const val TAG = "AudioPlayerService"

class AudioPlayerService : MediaBrowserServiceCompat() {
    private lateinit var notificationManager: AudioNotificationManager
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private val audioPlayerAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()
    private val playerListener = ExoPlayerEventListener()
    private val exoPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            setAudioAttributes(audioPlayerAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }

    private var isForegroundService = false

    override fun onCreate() {
        super.onCreate()

        // Build a PendingIntent that can be used to launch the UI.
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, 0)
            }

        // Create a new MediaSession.
        mediaSession = MediaSessionCompat(this, TAG)
            .apply {
                setSessionActivity(sessionActivityPendingIntent)
                isActive = true
            }

        notificationManager = AudioNotificationManager(
            this,
            exoPlayer,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )

        mediaSessionConnector = MediaSessionConnector(mediaSession).also { connector ->
            val dataSourceFactory = DefaultDataSourceFactory(
                this, Util.getUserAgent(this, getString(R.string.app_name)), null
            )

            val playBackPreparer =
                AudioPlaybackPreparer(
                    exoPlayer,
                    dataSourceFactory
                )

            connector.setPlayer(exoPlayer)
            connector.setPlaybackPreparer(playBackPreparer)
        }

        with(MediaMetadataCompat.Builder()) {
            putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, "Test title")
            putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, "Test subtitle")
            mediaSession.setMetadata(build())
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        if (TextUtils.equals(clientPackageName, packageName)) {
            return BrowserRoot(getString(R.string.app_name), null)
        }

        return null
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(null)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        exoPlayer.stop(true)
    }

    override fun onDestroy() {
        mediaSession.run {
            isActive = false
            release()
        }

        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

    private inner class ExoPlayerEventListener : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING,
                Player.STATE_READY -> {
                    notificationManager.showNotification()

                    if (playbackState == Player.STATE_READY && !playWhenReady) {
                        stopForeground(false)
                    }
                }

                else -> {
                    notificationManager.hideNotification()
                }
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            when (error.type) {
                // If the error occurs in a render component, Exoplayer raises a type_remote error.
                ExoPlaybackException.TYPE_RENDERER -> {
                    Timber.e("TYPE_RENDERER: ${error.rendererException.message}")
                }
                // If occurs an unexpected RuntimeException Exoplayer raises a type_unexpected error.
                ExoPlaybackException.TYPE_UNEXPECTED -> {
                    Timber.e("TYPE_UNEXPECTED: ${error.unexpectedException.message}")
                }
                // Occurs when there is a OutOfMemory error.
                ExoPlaybackException.TYPE_OUT_OF_MEMORY -> {
                    Timber.e("TYPE_OUT_OF_MEMORY: ${error.outOfMemoryError.message}")
                }
                // If the error occurs in a remote component, Exoplayer raises a type_remote error.
                ExoPlaybackException.TYPE_REMOTE -> {
                    Timber.e("TYPE_REMOTE: ${error.message}")
                }
                ExoPlaybackException.TYPE_SOURCE -> {
                    Timber.e("TYPE_SOURCE: ${error.sourceException.message}")
                }
            }
            Toast.makeText(
                applicationContext,
                R.string.generic_error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private inner class PlayerNotificationListener :
        PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, this@AudioPlayerService.javaClass)
                )

                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }
}
