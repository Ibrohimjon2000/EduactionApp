package uz.mobiler.lesson58_2.database.dao

import androidx.room.*
import uz.mobiler.lesson58_2.database.entity.Group

@Dao
interface GroupDao {
    @Insert
    fun addGroup(group: Group): Long

    @Update
    fun editGroup(group: Group)

    @Delete
    fun deleteGroup(group: Group)

    @Query("select *from `group` where mentor_id=:idM and course_id=:idC")
    fun getAllGroup(idM: Long, idC: Long): List<Group>

    @Query("select*from `group` where course_id=:idC")
    fun getAllIdGroup(idC: Long): List<Group>

    @Query("select*from `group` where course_id=:idC and is_open='Open'")
    fun getOpenAllIdGroup(idC: Long): List<Group>

    @Query("select*from `group` where course_id=:idC and is_open='Closed'")
    fun getClosedAllIdGroup(idC: Long): List<Group>

    @Query("select*from `group` where id=:id")
    fun getGroupById(id: Long): Group
}