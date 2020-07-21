package com.example.notesup.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.notesup.database.Note
import com.example.notesup.database.NotesDao
import kotlinx.coroutines.*

class HomeViewModel(
    private val notesDao: NotesDao,
    application: Application
) : AndroidViewModel(application) {

    val notesList = notesDao.retrieveAllNotes()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main+ job)

    init {
        notesList.value?.let {
            Log.i("HomeViewModel","The length of list is ${it.size}")
        }
        Log.i("HomeViewModel","Init done")
    }

    private val _navigateToAddNote = MutableLiveData<Boolean>()
    val navigateToAddNote: LiveData<Boolean>
    get() = _navigateToAddNote

    fun onAddNoteClicked(){
        _navigateToAddNote.value = true
    }

    fun onNavigateToAddNoteDone(){
        _navigateToAddNote.value = false
    }

    private val _navigateToSelectedNote = MutableLiveData<Note>()
    val navigateToSelectedNote: LiveData<Note>
    get() = _navigateToSelectedNote

    fun onNoteSelected(note: Note){
        _navigateToSelectedNote.value = note
    }

    fun onNavigateToSelectedNoteDone(){
        _navigateToSelectedNote.value = null
    }

    fun deleteNote(noteId: Long){
        uiScope.launch {
            deleteNoteFromDB(noteId)
        }
    }

    private suspend fun deleteNoteFromDB(noteId: Long) {
        return withContext(Dispatchers.IO){
            notesDao.deleteNote(noteId)
        }
    }

    fun archiveNote(note: Note){
        note.archived = true
        uiScope.launch {
            updateNoteinDB(note)
        }
    }

    private suspend fun updateNoteinDB(note: Note) {
        return withContext(Dispatchers.IO){
            notesDao.updateNote(note)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
