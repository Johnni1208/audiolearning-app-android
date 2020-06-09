package com.audiolearning.app.util.file

import android.net.Uri
import android.util.Log
import com.audiolearning.app.data.db.entities.Audio
import java.io.*

/**
 * Utilities class for AudioFiles in this project.
 */
class AudioFileUtils {
    companion object {
        /**
         * Moves the source file to destinationDirectory and deletes the old source file.
         *
         * @param sourceFile The file to be moved.
         * @param destinationDirectory The directory to be moved to.
         * @param destinationName The new name of the file. Can be left blank if name of the
         * sourceFile should be taken.
         */
        fun cutFileAndPasteToDirectory(
            sourceFile: File,
            destinationDirectory: String,
            destinationName: String? = null
        ) {
            try {
                //create output directory if it doesn't exist
                val dir = File(destinationDirectory)
                if (!dir.exists()) {
                    dir.mkdirs()
                }

                val inputStream = FileInputStream(sourceFile)

                val fileName = (destinationName ?: sourceFile.name) + Audio.fileExtension
                val outputStream =
                    FileOutputStream(destinationDirectory + File.separatorChar + fileName)

                val buffer = ByteArray(1024)
                val bufferedOutputStream = BufferedOutputStream(outputStream, buffer.size)

                var read = inputStream.read(buffer)
                while (read != -1) {
                    bufferedOutputStream.write(buffer, 0, read)
                    read = inputStream.read(buffer)
                }
                inputStream.close()

                // write the output file
                bufferedOutputStream.flush()
                bufferedOutputStream.close()

                //delete old file
                sourceFile.delete()
            } catch (fileNotFoundException: FileNotFoundException) {
                Log.e("tag", fileNotFoundException.message!!)
            } catch (e: Exception) {
                Log.e("tag", e.message!!)
            }
        }

        /**
         * Deletes the [Audio] from the device.
         *
         * @param audio The audio which should be deleted.
         */
        fun deleteAudioFile(audio: Audio) {
            val audioFile = File(Uri.parse(audio.fileUriString).path!!)
            audioFile.delete()
        }
    }
}