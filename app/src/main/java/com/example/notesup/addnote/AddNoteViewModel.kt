package com.example.notesup.addnote

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.notesup.database.Note
import com.example.notesup.database.NotesDao
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class AddNoteViewModel(
    private val dataSource: NotesDao,
    previousNote: Note?,
    application: Application
) : AndroidViewModel(application) {
    var note: Note
    var newNote = true
    private val job = Job()
    private val uiScope = CoroutineScope(job+Dispatchers.Main)

    init {
        note = previousNote?.let {
            newNote = false
            previousNote
        } ?: Note(description = null,timestamp = Date())
    }

    private suspend fun insertIntoDatabase(){
        return withContext(Dispatchers.IO){
            dataSource.insertNote(note)
        }
    }

    private suspend fun updateDatabase(){
        return withContext(Dispatchers.IO){
            dataSource.updateNote(note)
        }
    }

    override fun onCleared() {
        super.onCleared()
        note.timestamp = Date()
        uiScope.launch {
            if(newNote)
                insertIntoDatabase()
            else
                updateDatabase()
            Log.i("AddNoteViewModel","Successfully Inserted")
            job.cancel()
        }
    }
}
