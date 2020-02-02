package com.example.audiolearning.data.repositories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.audiolearning.data.db.AudioDao
import com.example.audiolearning.data.db.AudioLearningDatabase
import com.example.audiolearning.data.db.entities.Subject
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import java.io.File
import java.io.IOException

@Suppress("BlockingMethodInNonBlockingContext")
@RunWith(AndroidJUnit4::class)
class AudioRepositoryTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val tempFolderFile = TemporaryFolder()

    private lateinit var audioRepository: AudioRepository
    private lateinit var audioDao: AudioDao
    private lateinit var db: AudioLearningDatabase

    private val testAudioName = "testAudio"
    private lateinit var testTempFile: File

    private lateinit var testSubject: Subject
    private val testSubjectName = "testSubject"
    private lateinit var testSubjectFolder: File

    @Before
    fun setUpDatabase() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AudioLearningDatabase::class.java
        ).allowMainThreadQueries().build()

        audioRepository = AudioRepository(db)
        audioDao = db.getAudioDao()

        with(SubjectRepository(db, tempFolderFile.root)) {
            this.insert(testSubjectName)
            testSubject = this.getSubjectByName(testSubjectName)!!
        }

        testSubjectFolder =
            File(tempFolderFile.root.path + File.separatorChar + "subjects" + File.separatorChar + testSubjectName)

        testTempFile = File.createTempFile("testTempFile", ".m4a")
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_ShouldMoveAudioFileToSubjectDirectory() = runBlocking {
        audioRepository.insert(testTempFile, testAudioName, testSubject)

        assertTrue(testSubjectFolder.listFiles()?.find { it.name == "$testAudioName.m4a" } != null)
    }

    @Test
    fun insert_ShouldInsertANewAudio() = runBlocking {
        audioRepository.insert(testTempFile, testAudioName, testSubject)

        assertTrue(audioDao.getAudioByName(testAudioName) != null)
    }
}