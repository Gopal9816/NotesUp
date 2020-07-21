package com.example.notesup.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class],version = 2, exportSchema = false)
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
                        .addMigrations(MIGRATION_1_2)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE new_notes_table (
                uid INTEGER PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                description TEXT,
                timestamp INTEGER,
                archived INTEGER  NOT NULL DEFAULT 0
                )
        """.trimIndent())

        database.execSQL("""
            INSERT INTO new_notes_table(uid,title,description,timestamp)
            SELECT uid,title,description,timestamp FROM notes_table
        """.trimIndent())

        database.execSQL("DROP TABLE notes_table")
        database.execSQL("ALTER TABLE new_notes_table RENAME TO notes_table")
    }
}