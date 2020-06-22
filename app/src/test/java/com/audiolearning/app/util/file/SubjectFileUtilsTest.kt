package com.audiolearning.app.util.file

import com.audiolearning.app.data.db.entities.Subject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class SubjectFileUtilsTest {
    @get:Rule
    val tempFolderFile = TemporaryFolder()

    private lateinit var subjectsFolder: File
    private val testSubjectName = "testSubject"

    private lateinit var expectedFolder: File

    @Before
    fun setUp() {
        subjectsFolder = File(tempFolderFile.root.path + File.separatorChar + "subjects")
        expectedFolder = File(subjectsFolder.path + File.separatorChar + testSubjectName)
    }

    @Test
    fun createNewSubjectDirectory_ShouldCreateNewDirectoryOnDevice() {
        SubjectFileUtils.createNewSubjectDirectory(tempFolderFile.root, testSubjectName)

        assertTrue(expectedFolder.exists())
    }

    @Test
    fun createNewSubjectDirectory_ShouldReturnTheNewDirectory() {
        val returnedSubjectFolder =
            SubjectFileUtils.createNewSubjectDirectory(tempFolderFile.root, testSubjectName)

        assertEquals(expectedFolder, returnedSubjectFolder)
    }

    @Test
    fun deleteSubjectDirectory_ShouldDeleteTheDirectoryOfASubject() {
        val testSubjectFolder =
            SubjectFileUtils.createNewSubjectDirectory(tempFolderFile.root, testSubjectName)
        val testSubject = Subject(testSubjectName, testSubjectFolder.toURI().toString())

        SubjectFileUtils.deleteSubjectDirectory(testSubject)

        assertFalse(testSubjectFolder.exists())
    }
}
