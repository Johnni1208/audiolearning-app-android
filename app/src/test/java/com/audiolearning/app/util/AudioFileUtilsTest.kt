package com.audiolearning.app.util

import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.util.file.AudioFileUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class AudioFileUtilsTest {
    @get:Rule
    val tempFolderFile = TemporaryFolder()

    @Test
    fun cutFileAndPasteToDirectory_ShouldDeleteOldFile() {
        val tempSourceFolder = tempFolderFile.newFolder("source")
        val fileToBeDeleted = File(tempSourceFolder.path + "/testFile")
        fileToBeDeleted.createNewFile()

        AudioFileUtils.cutFileAndPasteToDirectory(fileToBeDeleted, "")

        assertFalse(fileToBeDeleted.exists())
    }

    @Test
    fun cutFileAndPasteToDirectory_ShouldCreateNewFileAtDestination() {
        val tempSourceFolder = tempFolderFile.newFolder("source")
        val fileToBeDeleted = File(tempSourceFolder.path + "/testFile")
        fileToBeDeleted.createNewFile()
        val tempDestinationFolder = tempFolderFile.newFolder("destination")

        AudioFileUtils.cutFileAndPasteToDirectory(fileToBeDeleted, tempDestinationFolder.path)

        val expectedFile =
            File(tempDestinationFolder.path + "/testFile" + Audio.fileExtension)
        assertTrue(expectedFile.exists())
    }
}