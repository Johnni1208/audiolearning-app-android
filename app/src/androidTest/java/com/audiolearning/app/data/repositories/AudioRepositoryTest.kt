package com.audiolearning.app.data.repositories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.audiolearning.app.data.db.AudioDao
import com.audiolearning.app.data.db.AudioLearningDatabase
import com.audiolearning.app.data.db.entities.Subject
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
    val tempFolder = TemporaryFolder()

    private lateinit var audioRepository: AudioRepository
    private lateinit var audioDao: AudioDao
    private lateinit var db: AudioLearningDatabase

    private val testAudioName = "testAudio"
    private lateinit var testAudioFile: File

    private lateinit var testSubject: Subject
    private val testSubjectName = "testSubject"
    private lateinit var testSubjectFolder: File

    @Before
    fun setUpDatabaseAndFiles() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AudioLearningDatabase::class.java
        ).allowMainThreadQueries().build()

        audioRepository = AudioRepository(db)
        audioDao = db.getAudioDao()

        with(SubjectRepository(db, tempFolder.root)) {
            this.insert(testSubjectName)
            testSubject = this.getSubjectByName(testSubjectName)!!
        }

        testSubjectFolder =
            File(tempFolder.root.path + File.separatorChar + "subjects" + File.separatorChar + testSubjectName)

        testAudioFile = File.createTempFile("testTempFile", ".m4a")
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_ShouldMoveAudioFileToSubjectDirectory() = runBlocking {
        audioRepository.insert(testAudioFile, testAudioName, testSubject)

        assertTrue(testSubjectFolder.listFiles()?.find { it.name == "$testAudioName.m4a" } != null)
    }

    @Test
    fun insert_ShouldInsertANewAudioIntoTheDb() = runBlocking {
        audioRepository.insert(testAudioFile, testAudioName, testSubject)

        assertTrue(audioDao.getAudioByName(testAudioName) != null)
    }
}