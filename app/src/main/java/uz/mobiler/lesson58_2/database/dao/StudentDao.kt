package uz.mobiler.lesson58_2.database.dao

import androidx.room.*
import uz.mobiler.lesson58_2.database.entity.Student

@Dao
interface StudentDao {
    @Insert
    fun addStudent(student: Student): Long

    @Delete
    fun deleteStudent(student: Student)

    @Update
    fun editStudent(student: Student)

    @Query("select *from student where group_id=:idG")
    fun getAllStudent(idG: Long): List<Student>
}