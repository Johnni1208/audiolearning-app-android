package com.audiolearning.app.data.repositories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.audiolearning.app.data.db.AudioDao
import com.audiolearning.app.data.db.AudioLearningDatabase
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.getTestValue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.io.IOException

@Suppress("BlockingMethodInNonBlockingContext")
@RunWith(RobolectricTestRunner::class)
class AudioRepositoryTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var audioRepository: AudioRepository
    private lateinit var audioDao: AudioDao
    private lateinit var db: AudioLearningDatabase

    private lateinit var testAudio: Audio
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

        testAudio = Audio("testAudio", testAudioFile.toURI().toString(), 0, testSubject.id!!)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun delete_ShouldDeleteAudioFromDb() = runBlocking {
        audioDao.insert(testAudio)
        val testAudioFromDb = audioDao.getAudioByName(testAudio.name)!!

        audioRepository.delete(testAudioFromDb)

        assertEquals(audioDao.getAudioByName(testAudio.name), null)
    }

    @Test
    fun delete_ShouldDeleteAudioFromDevice() = runBlocking {
        audioRepository.delete(testAudio)

        assertFalse(testAudioFile.exists())
    }

    @Test
    fun getAudiosOfSubject_ShouldReturnCorrectAudios() = runBlocking {
        audioDao.insert(testAudio)

        assertEquals(
            testAudio.name,
            audioRepository.getAudiosOfSubject(testAudio.subjectId).getTestValue()[0].name
        )
    }

    @Test
    fun getAudioById_ShouldReturnCorrectAudio() = runBlocking {
        audioDao.insert(testAudio)
        val dbAudio = audioDao.getAudioByName(testAudio.name)!!

        assertEquals(dbAudio, audioRepository.getAudioById(dbAudio.id!!))
    }
}
