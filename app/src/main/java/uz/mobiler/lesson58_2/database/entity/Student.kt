package uz.mobiler.lesson58_2.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("group_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Mentor::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("mentor_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "first_name")
    var firstName: String,
    @ColumnInfo(name = "last_name")
    var lastName: String,
    var patron: String,
    @ColumnInfo(name = "register_date")
    var registerDate: String,
    @ColumnInfo(name = "group_id")
    val groupId: Long,
    @ColumnInfo(name = "mentor_id")
    val mentorId: Long
) : Serializable
