package com.example.notesup.addnote

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesup.database.Note
import com.example.notesup.database.NotesDao
import java.lang.IllegalArgumentException

class AddNoteViewModelFactory(
    val dataSource: NotesDao,
    val note: Note?,
    val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNoteViewModel::class.java))
            return AddNoteViewModel(dataSource,note,application) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}