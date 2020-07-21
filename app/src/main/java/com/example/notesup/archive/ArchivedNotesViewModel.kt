package com.example.notesup.archive

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesup.database.Note
import com.example.notesup.database.NotesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ArchivedNotesViewModel(
    private val dataSource: NotesDao,
    application: Application
) : AndroidViewModel(application) {
    val notesList = dataSource.retrieveAllArchivedNotes()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main+ job)

    private val _navigateToSelectedNote = MutableLiveData<Note>()
    val navigateToSelectedNote: LiveData<Note>
        get() = _navigateToSelectedNote

    fun onNoteSelected(note: Note){
        _navigateToSelectedNote.value = note
    }

    fun onNavigateToSelectedNoteDone(){
        _navigateToSelectedNote.value = null
    }
}