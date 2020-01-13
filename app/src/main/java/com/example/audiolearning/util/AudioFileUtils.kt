package com.example.audiolearning.util

import android.util.Log
import java.io.*

/**
 * Utilities class for AudioFiles in this project.
 */
class AudioFileUtils {
    companion object {
        /**
         * Copies the file to destinationDirectory and
         *
         * @param sourceFile The file to be moved.
         * @param destinationDirectory The directory to be moved to.
         * @param destinationName The new name of the file. Can be left blank if name of the
         * sourceFile should be taken.
         */
        fun moveFile(
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

                val fileName = (destinationName ?: sourceFile.name) + ".m4a"
                val outputStream = FileOutputStream("$destinationDirectory/$fileName")

                val buffer = ByteArray(1024)
                val bufferedOutputStream = BufferedOutputStream(outputStream, buffer.size)

                var read = inputStream.read(buffer)
                while (read != -1) {
                    bufferedOutputStream.write(buffer, 0, read)
                    read = inputStream.read(buffer) // if read value is -1, it escapes loop.
                }
                inputStream.close()

                // write the output file (You have now copied the file)
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
    }
}