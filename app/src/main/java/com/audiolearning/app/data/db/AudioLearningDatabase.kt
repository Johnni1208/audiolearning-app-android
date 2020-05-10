package com.audiolearning.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject

@Database(
    entities = [Audio::class, Subject::class],
    version = 4
)
abstract class AudioLearningDatabase : RoomDatabase() {
    abstract fun getAudioDao(): AudioDao
    abstract fun getSubjectDao(): SubjectDao

    companion object {
        @Volatile
        private var instance: AudioLearningDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: createDatabase(
                        context
                    ).also { instance = it }
            }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AudioLearningDatabase::class.java, "AudioLearningDB.db"
            ).fallbackToDestructiveMigration().build()
    }
}