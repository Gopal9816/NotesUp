package com.example.notesup.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Note::class],version = 1, exportSchema = false)
@TypeConverters(Convertors::class)
abstract class NotesUpDatabase: RoomDatabase() {
    abstract val notesTableDao: NotesDao

    companion object{

        @Volatile
        private var INSTANCE: NotesUpDatabase? = null

        fun getInstance(context: Context): NotesUpDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        NotesUpDatabase::class.java,
                        "notes_up_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}