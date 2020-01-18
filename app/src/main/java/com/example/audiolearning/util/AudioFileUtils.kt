package com.example.audiolearning.util

import android.util.Log
import java.io.*
import java.util.regex.Pattern

/**
 * Utilities class for AudioFiles in this project.
 */
class AudioFileUtils {
    companion object {
        const val fileExtension = ".m4a"

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

                val fileName = (destinationName ?: sourceFile.name) + fileExtension
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

        /**
         * Checks whether the given file name has characters which are not allowed.
         *
         * Reserved charaters: \|?*<":>/'
         *
         * @param name The file name to be checked
         *
         * @return True - If name does not contain any reserved characters
         * False - If name does return any reserved characters
         */
        fun isFileNameAllowed(name: String): Boolean {
            val reservedCharacters = Pattern.compile("[\\\\|?*<\":>/']")
            return !reservedCharacters.matcher(name).find()
        }
    }
}