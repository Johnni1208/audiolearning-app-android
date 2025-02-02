package com.audiolearning.app.service.audioplayer

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.audiolearning.app.extension.from
import com.audiolearning.app.extension.toMediaSource
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DataSource

class AudioPlaybackPreparer(
    private val exoPlayer: ExoPlayer,
    private val dataSourceFactory: DataSource.Factory
) : MediaSessionConnector.PlaybackPreparer {
    override fun getSupportedPrepareActions(): Long =
        PlaybackStateCompat.ACTION_PREPARE_FROM_URI or
                PlaybackStateCompat.ACTION_PLAY_FROM_URI

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) {
        val metaData = MediaMetadataCompat.Builder()
            .from(extras ?: throw IllegalArgumentException("Missing argument $extras")).build()
        val mediaSource = metaData.toMediaSource(dataSourceFactory)

        exoPlayer.prepare(mediaSource)
        exoPlayer.playWhenReady = playWhenReady
    }

    override fun onPrepare(playWhenReady: Boolean) = Unit

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) = Unit

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) =
        Unit

    override fun onCommand(
        player: Player,
        controlDispatcher: ControlDispatcher,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ) = false
}
