package uz.mobiler.lesson58_2.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.mobiler.lesson58_2.database.entity.Course

@Dao
interface CourseDao {
    @Insert
    fun addCourse(course: Course)

    @Query("select*from course where id=:id")
    fun getCourseById(id: Long): Course

    @Query("select*from course")
    fun getAllCourses(): List<Course>
}