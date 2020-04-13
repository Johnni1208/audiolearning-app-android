package com.audiolearning.app.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class Subject(
    @ColumnInfo(name = "subject_name")
    override val name: String,

    @ColumnInfo(name = "subject_directory_uri")
    val directoryUriString: String
) : BaseEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subject_id")
    override var id: Long? = null

    /**
     * Used for placeholder items in a subject spinner.
     *
     * (Placeholders like: "Select subject..." or "Add subject..."
     *
     * If items has to be a placeholder -> [isRealSubject] = false
     */
    @Ignore
    var isRealSubject: Boolean = true
}