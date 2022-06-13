package uz.mobiler.lesson58_2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.mobiler.lesson58_2.database.dao.CourseDao
import uz.mobiler.lesson58_2.database.dao.GroupDao
import uz.mobiler.lesson58_2.database.dao.MentorDao
import uz.mobiler.lesson58_2.database.dao.StudentDao
import uz.mobiler.lesson58_2.database.entity.Course
import uz.mobiler.lesson58_2.database.entity.Group
import uz.mobiler.lesson58_2.database.entity.Mentor
import uz.mobiler.lesson58_2.database.entity.Student

@Database(entities = [Course::class, Group::class, Mentor::class, Student::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun groupDao(): GroupDao
    abstract fun mentorDao(): MentorDao
    abstract fun studentDao(): StudentDao

    companion object {
        private var appDatabase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
                    .allowMainThreadQueries()
                    .build()
            }
            return appDatabase!!
        }
    }
}
