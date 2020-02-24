package com.audiolearning.app.data.repositories

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.audiolearning.app.data.db.AudioDao
import com.audiolearning.app.data.db.AudioLearningDatabase
import com.audiolearning.app.data.db.SubjectDao
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.util.getTestValue
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
    val tempFolder = TemporaryFolder()

    private lateinit var subjectRepository: SubjectRepository
    private lateinit var subjectDao: SubjectDao
    private lateinit var audioDao: AudioDao
    private lateinit var db: AudioLearningDatabase
    private lateinit var subjectsRootFolder: File

    private val testSubjectName = "testSubject"
    private lateinit var testSubjectFolder: File

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

        assertEquals(expectedSubject, subjectDao.getAllSubjects().getTestValue()[0])
    }

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
}