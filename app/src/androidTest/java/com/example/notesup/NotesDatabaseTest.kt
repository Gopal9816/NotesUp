package com.example.notesup

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.notesup.database.Note
import com.example.notesup.database.NotesDao
import com.example.notesup.database.NotesUpDatabase
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class NotesDatabaseTest {

    private lateinit var database: NotesUpDatabase
    private lateinit var notesDao: NotesDao

    @Before
    fun createDB(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context,NotesUpDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        notesDao = database.notesTableDao
    }

    @Test
    fun insertNote(){
        var note = Note(description = "Test",timestamp = Date())
        var res = notesDao.insertNote(note)

        println("The number of rows inserted is ${res.size}")
        assertEquals(1,res.size)
    }
}