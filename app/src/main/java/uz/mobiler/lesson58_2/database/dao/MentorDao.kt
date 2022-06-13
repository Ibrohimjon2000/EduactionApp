package uz.mobiler.lesson58_2.database.dao

import androidx.room.*
import uz.mobiler.lesson58_2.database.entity.Mentor

@Dao
interface MentorDao {
    @Insert
    fun addMentor(mentor: Mentor)

    @Update
    fun editMentor(mentor: Mentor)

    @Delete
    fun deleteMentor(mentor: Mentor)

    @Query("select *from mentor where course_id=:ids")
    fun getAllMentors(ids: Long): List<Mentor>
}