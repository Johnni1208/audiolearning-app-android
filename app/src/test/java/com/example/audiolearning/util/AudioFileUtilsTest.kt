package com.example.audiolearning.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class AudioFileUtilsTest {

    @Test
    fun cutFileAndPasteToDirectory_ShouldDeleteOldFile() {
        val fileToBeDeleted = File("")

        AudioFileUtils.cutFileAndPasteToDirectory(fileToBeDeleted, "")
        assertFalse(fileToBeDeleted.exists())
    }

    @get:Rule
    val tempFolderFile = TemporaryFolder()

    @Test
    fun cutFileAndPasteToDirectory_ShouldCreateNewFileAtDestination() {
        val tempSourceFolder = tempFolderFile.newFolder("source")
        val fileToBeDeleted = File(tempSourceFolder.path + "/testFile")
        fileToBeDeleted.createNewFile()

        val tempDestinationFolder = tempFolderFile.newFolder("destination")

        AudioFileUtils.cutFileAndPasteToDirectory(fileToBeDeleted, tempDestinationFolder.path)

        val expectedFile =
            File(tempDestinationFolder.path + "/testFile" + AudioFileUtils.fileExtension)

        assertTrue(expectedFile.exists())
    }
}