package com.audiolearning.app

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
open class AudioLearningApplication : Application() {
    companion object {
        private lateinit var instance: AudioLearningApplication

        val appContext: Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())
    }
}
