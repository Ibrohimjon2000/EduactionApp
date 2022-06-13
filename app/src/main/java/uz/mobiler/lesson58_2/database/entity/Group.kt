package uz.mobiler.lesson58_2.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("course_id")
        ),
        ForeignKey(
            entity = Mentor::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("mentor_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Group(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    @ColumnInfo(name = "is_open")
    var isOpen: String,
    var date: String,
    @ColumnInfo(name = "date_type")
    var dateType: String,
    @ColumnInfo(name = "course_id")
    val courseId: Long,
    @ColumnInfo(name = "mentor_id")
    val mentorId: Long
) : Serializable
