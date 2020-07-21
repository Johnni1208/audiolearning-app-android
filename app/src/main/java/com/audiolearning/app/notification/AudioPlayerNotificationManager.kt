package com.audiolearning.app.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.audiolearning.app.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

private const val REWIND_MS = 10000L
private const val FAST_FORWARD_MS = 10000L

class AudioNotificationManager(
    context: Context,
    private val player: ExoPlayer,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener
) {
    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)

        notificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            context.getString(R.string.audio_notification_channel_id),
            R.string.audio_notification_name,
            R.string.audio_notification_description,
            context.resources.getInteger(R.integer.notification_id),
            DescriptionAdapter(mediaController),
            notificationListener
        ).apply {
            setMediaSessionToken(sessionToken)
            setPriority(NotificationCompat.PRIORITY_MAX)
            setSmallIcon(R.drawable.ic_audio_notification)
            // TODO: 02.07.2020 Implement notification icon
            setRewindIncrementMs(REWIND_MS)
            setFastForwardIncrementMs(FAST_FORWARD_MS)
            setUseChronometer(false)
            setUseNavigationActions(false)
            setColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotification() {
        notificationManager.setPlayer(player)
    }

    private inner class DescriptionAdapter(private val controller: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {
        override fun createCurrentContentIntent(player: Player): PendingIntent? =
            controller.sessionActivity

        override fun getCurrentContentTitle(player: Player) =
            controller.metadata.description.title.toString()

        override fun getCurrentContentText(player: Player) =
            controller.metadata.description.subtitle.toString()

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? = null

        // TODO: 02.07.2020 Implement Icon from current music
    }
}
