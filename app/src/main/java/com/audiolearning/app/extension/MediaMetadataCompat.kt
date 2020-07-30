package com.audiolearning.app.extension

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource

/**
 * Useful extensions for [MediaMetadataCompat]
 */

inline val MediaMetadataCompat.id: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)

inline val MediaMetadataCompat.title: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_TITLE)

inline val MediaMetadataCompat.mediaUri: Uri
    get() = this.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri()

inline val MediaMetadataCompat.duration
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DURATION)

inline val MediaMetadataCompat.date: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION)

inline val MediaMetadataCompat.displaySubtitle: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE)

/**
 * Useful extensions for [MediaMetadataCompat.Builder].
 */

// These do not have getters, so create a message for the error.
const val NO_GET = "Property does not have a 'get'"
const val ERROR_CANNOT_GET = "Cannot get from MediaMetadataCompat.Builder"

inline var MediaMetadataCompat.Builder.id: String
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException(ERROR_CANNOT_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, value)
    }

inline var MediaMetadataCompat.Builder.title: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException(ERROR_CANNOT_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_TITLE, value)
    }

inline var MediaMetadataCompat.Builder.artist: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException(ERROR_CANNOT_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_ARTIST, value)
        putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, value)
    }

inline var MediaMetadataCompat.Builder.album: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException(ERROR_CANNOT_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_ALBUM, value)
    }

inline var MediaMetadataCompat.Builder.duration: Long
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException(ERROR_CANNOT_GET)
    set(value) {
        putLong(MediaMetadataCompat.METADATA_KEY_DURATION, value)
    }

inline var MediaMetadataCompat.Builder.mediaUri: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException(ERROR_CANNOT_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, value)
    }

inline var MediaMetadataCompat.Builder.displayTitle: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException(ERROR_CANNOT_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, value)
    }

inline var MediaMetadataCompat.Builder.displaySubtitle: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException(ERROR_CANNOT_GET)
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, value)
    }

inline var MediaMetadataCompat.Builder.date: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException(ERROR_CANNOT_GET)
    set(value) {
        // We need to put it into the description, since MediaMetadataCompat.METADATA_KEY_DATE cannot be get afterwards
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, value)
    }

/**
 * Custom property for retrieving [MediaMetadataCompat]. Also includes the rest of keys
 * from the [MediaMetadataCompat] in its extras.
 *
 * These keys are used by the ExoPlayers MediaSession extension
 */
inline val MediaMetadataCompat.fullDescription: MediaDescriptionCompat
    get() =
        description.also {
            it.extras?.putAll(bundle)
        }

/**
 * Converts [MediaMetadataCompat] to [ProgressiveMediaSource]. This can then be used by the ExoPlayer.
 */
fun MediaMetadataCompat.toMediaSource(dataSourceFactory: DataSource.Factory): ProgressiveMediaSource =
    ProgressiveMediaSource.Factory(dataSourceFactory)
        .setTag(fullDescription)
        .createMediaSource(mediaUri)

/**
 * Helper function to convert a [Bundle] to [MediaMetadataCompat.Builder].
 * We chose the Builder since you can then make other actions on it.
 */
fun MediaMetadataCompat.Builder.from(bundle: Bundle): MediaMetadataCompat.Builder {
    id = bundle.audioId
    title = bundle.audioName
    duration = bundle.audioDuration
    mediaUri = bundle.audioUri
    artist = bundle.subjectName
    album = bundle.subjectName
    date = bundle.audioDate.toFormattedDate()

    displayTitle = bundle.audioName
    displaySubtitle = bundle.subjectName

    // Return this Builder instance, to further work with it
    return this
}
