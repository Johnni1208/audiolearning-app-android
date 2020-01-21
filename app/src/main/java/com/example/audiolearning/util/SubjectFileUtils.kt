package com.example.audiolearning.util

import java.io.File

class SubjectFileUtils {
    companion object {
        fun createNewSubjectDirectory(filesDir: File, directoryName: String): File {
            val rootDir = filesDir.path + File.separatorChar + "subjects"

            val subjectDir = File(rootDir + File.separatorChar + directoryName)

            if (!subjectDir.exists()) {
                subjectDir.mkdirs()
            }

            return subjectDir
        }
    }
}