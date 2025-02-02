package com.audiolearning.app.data.repositories

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.audiolearning.app.data.db.AudioDao
import com.audiolearning.app.data.db.AudioLearningDatabase
import com.audiolearning.app.data.db.SubjectDao
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.getTestValue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
class SubjectRepositoryTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var subjectRepository: SubjectRepository
    private lateinit var subjectDao: SubjectDao
    private lateinit var audioDao: AudioDao
    private lateinit var db: AudioLearningDatabase
    private lateinit var subjectsRootFolder: File

    private val testSubjectName = "testSubject"
    private lateinit var testSubjectFolder: File
    private val testSubjectId = 1

    @Before
    fun setUpDatabaseAndFolders() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AudioLearningDatabase::class.java
        ).allowMainThreadQueries().build()

        subjectRepository = SubjectRepository(db, tempFolder.root)
        subjectDao = db.getSubjectDao()
        audioDao = db.getAudioDao()
        subjectsRootFolder = File(tempFolder.root.path + File.separatorChar + "subjects")

        testSubjectFolder = File(subjectsRootFolder.path + File.separatorChar + testSubjectName)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_ShouldCreateFolderOnDevice() = runBlocking {
        subjectRepository.insert(testSubjectName)

        assertEquals(
            subjectsRootFolder.listFiles()?.find { it.name == testSubjectName },
            testSubjectFolder
        )
    }

    @Test
    fun insert_ShouldInsertANewSubjectIntoTheDb() = runBlocking {
        val expectedSubject =
            Subject(testSubjectName, Uri.fromFile(testSubjectFolder).toString())

        subjectRepository.insert(testSubjectName)

        val actualSubject = subjectDao.getAllSubjects().getTestValue()[0]

        assertEquals(expectedSubject, actualSubject)
    }

    /**
     * Only runs on non-windows-machines, because the file uri gets not set correctly.
     */
    @Test
    fun delete_ShouldDeleteFolderOnDevice() = runBlocking {
        subjectRepository.insert(testSubjectName)
        val testSubject = subjectDao.getSubjectByName(testSubjectName)

        subjectRepository.delete(testSubject!!)

        assertTrue(subjectsRootFolder.listFiles()?.find { it.name == testSubjectName } == null)
    }

    @Test
    fun delete_ShouldDeleteSubjectFromDB() = runBlocking {
        subjectRepository.insert(testSubjectName)
        val testSubject = subjectDao.getSubjectByName(testSubjectName)

        subjectRepository.delete(testSubject!!)

        assertTrue(subjectDao.getSubjectByName(testSubjectName) == null)
    }

    @Test
    fun delete_ShouldDeleteAllChildsOfSubjectFromDb() = runBlocking {
        subjectRepository.insert(testSubjectName)
        val testSubject = subjectDao.getSubjectByName(testSubjectName)
        val testAudio = Audio(
            "testAudio",
            "",
            0,
            testSubject?.id!!
        )
        audioDao.insert(testAudio)

        subjectRepository.delete(testSubject)

        assertTrue(audioDao.getAllAudios().isEmpty())
    }

    @Test
    fun getAllSubjects_ShouldReturnAllSubjects() = runBlocking {
        val testSubjects = listOf(
            Subject("Subject 1", ""),
            Subject("Subject 2", ""),
            Subject("Subject 3", ""),
            Subject("Subject 4", "")
        )

        testSubjects.forEach {
            subjectDao.insert(it)
        }

        assertEquals(testSubjects, subjectRepository.getAllSubjects().getTestValue())
    }

    @Test
    fun getSubjectByName_ShouldReturnSpecifiedSubject() = runBlocking {
        val testSubject = Subject(testSubjectName, "")
        subjectDao.insert(testSubject)

        assertEquals(testSubject, subjectRepository.getSubjectByName(testSubjectName))
    }

    @Test
    fun getSubjectById_ShouldReturnSpecifiedSubject() = runBlocking {
        val testSubject = Subject(testSubjectName, "").apply { id = testSubjectId }
        subjectDao.insert(testSubject)

        assertEquals(testSubject.id, subjectRepository.getSubjectById(testSubjectId)?.id)
    }
}
