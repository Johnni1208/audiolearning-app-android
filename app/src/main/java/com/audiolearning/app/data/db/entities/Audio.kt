package com.audiolearning.app.data.db.entities

import androidx.room.*

@Entity(
    tableName = "audios",
    foreignKeys = [ForeignKey(
        entity = Subject::class,
        parentColumns = ["subject_id"],
        childColumns = ["audio_subject_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Audio(
    @ColumnInfo(name = "audio_name")
    val name: String,

    @ColumnInfo(name = "audio_file_uri")
    val audioFileUriString: String,

    @ColumnInfo(name = "audio_duration")
    val durationInMilliseconds: Int,

    @ColumnInfo(name = "audio_subject_id")
    val subjectId: Long,

    @ColumnInfo(name = "create_date")
    val createDate: Long = System.currentTimeMillis()
) : BaseEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "audio_id")
    override var id: Long? = null

    companion object {
        @Ignore
        val fileExtension = ".m4a"
    }
}