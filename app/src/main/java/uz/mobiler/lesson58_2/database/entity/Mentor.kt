package uz.mobiler.lesson58_2.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("course_id")
        )
    ]
)
data class Mentor(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    @ColumnInfo(name = "first_name")
    var firstName: String,
    @ColumnInfo(name = "last_name")
    var lastName: String,
    var patron: String,
    @ColumnInfo(name = "course_id")
    val courseId: Long
)

