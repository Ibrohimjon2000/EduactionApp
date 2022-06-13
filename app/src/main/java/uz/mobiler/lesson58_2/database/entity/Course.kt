package uz.mobiler.lesson58_2.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0,
    var name:String,
    var description: String
)
