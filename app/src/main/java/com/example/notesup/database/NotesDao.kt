package com.example.notesup.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {
    @Insert
    fun insertNote(vararg note: Note)

    @Update
    fun updateNote(note: Note)

    @Query("SELECT * FROM notes_table where archived=0 ORDER BY timestamp DESC")
    fun retrieveAllNotes(): LiveData<List<Note>>

    @Query("DELETE FROM notes_table WHERE uid = :uid")
    fun deleteNote(uid:Long)

    @Query("SELECT * FROM notes_table WHERE archived=1 ORDER BY timestamp DESC")
    fun retrieveAllArchivedNotes():LiveData<List<Note>>
}