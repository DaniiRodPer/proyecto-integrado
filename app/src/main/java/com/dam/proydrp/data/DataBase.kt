package com.dam.proydrp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dam.proydrp.data.converters.Converters
import com.dam.proydrp.data.dao.UserDao
import com.dam.proydrp.data.model.UserProfile
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

@Database(
    version = 1,
    entities = [UserProfile::class],
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "proydrp_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadExecutor().execute {
                                INSTANCE?.let { database ->
                                    prepopulateDatabase(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun prepopulateDatabase(database: AppDatabase) {
            val userDao = database.userDao()

            runBlocking {

            }
        }
    }
}