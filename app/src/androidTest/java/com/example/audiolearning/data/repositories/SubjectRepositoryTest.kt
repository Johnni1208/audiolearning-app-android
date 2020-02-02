package com.example.audiolearning.data.repositories

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.audiolearning.data.db.AudioDao
import com.example.audiolearning.data.db.AudioLearningDatabase
import com.example.audiolearning.data.db.SubjectDao
import com.example.audiolearning.data.db.entities.Audio
import com.example.audiolearning.data.db.entities.Subject
import com.example.audiolearning.util.getTestValue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
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
class SubjectRepositoryTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val tempFolderFile = TemporaryFolder()

    private lateinit var subjectRepository: SubjectRepository
    private lateinit var subjectDao: SubjectDao
    private lateinit var audioDao: AudioDao
    private lateinit var db: AudioLearningDatabase
    private lateinit var subjectRootFile: File

    private val testSubjectName = "test"
    private lateinit var testSubjectFolder: File

    @Before
    fun setUpDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AudioLearningDatabase::class.java
        ).allowMainThreadQueries().build()

        subjectRepository = SubjectRepository(db, tempFolderFile.root)
        subjectDao = db.getSubjectDao()
        audioDao = db.getAudioDao()
        subjectRootFile = File(tempFolderFile.root.path + File.separatorChar + "subjects")

        testSubjectFolder = File(subjectRootFile.path + File.separatorChar + testSubjectName)
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
            subjectRootFile.listFiles()?.find { it.name == testSubjectName },
            testSubjectFolder
        )
    }

    @Test
    fun insert_ShouldInsertANewSubject() = runBlocking {
        val expectedSubject =
            Subject(testSubjectName, Uri.fromFile(testSubjectFolder).toString())

        subjectRepository.insert(testSubjectName)

        assertEquals(expectedSubject, subjectDao.getAllSubjects().getTestValue()[0])
    }

    @Test
    fun delete_ShouldDeleteFolderOnDevice() = runBlocking {
        subjectRepository.insert(testSubjectName)
        val testSubject = subjectDao.getSubjectByName(testSubjectName)

        subjectRepository.delete(testSubject!!)

        assertTrue(subjectRootFile.listFiles()?.find { it.name == testSubjectName } == null)
    }

    @Test
    fun delete_ShouldDeleteSubjectFromDB() = runBlocking {
        subjectRepository.insert(testSubjectName)
        val testSubject = subjectDao.getSubjectByName(testSubjectName)

        subjectRepository.delete(testSubject!!)

        assertTrue(subjectDao.getSubjectByName(testSubjectName) == null)
    }

    @Test
    fun delete_ShouldDeleteAllChildsOfSubject() = runBlocking {
        subjectRepository.insert(testSubjectName)
        val testSubject = subjectDao.getSubjectByName(testSubjectName)
        val testAudio = Audio(
            "testAudio",
            testSubject?.id!!,
            ""
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
}