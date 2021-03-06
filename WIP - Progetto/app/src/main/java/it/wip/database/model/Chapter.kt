package it.wip.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "chapter",
        foreignKeys = [
            ForeignKey(
                entity = Story::class,
                parentColumns = ["id"],
                childColumns = ["story"],
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE
            )
        ])
data class Chapter(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "chapter_name")
    val chapterName: String,
    val time: String,
    @ColumnInfo(name = "created_on")
    val createdOn: String,
    @ColumnInfo(name = "study_time")
    val studyTime: Int,
    @ColumnInfo(name = "break_time")
    val breakTime: Int,
    val mode: Int,
    val avatar: String,
    val story: Int
)