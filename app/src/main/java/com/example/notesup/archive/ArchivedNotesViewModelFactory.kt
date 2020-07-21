package com.example.notesup.archive

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesup.database.NotesDao
import java.lang.IllegalArgumentException

class ArchivedNotesViewModelFactory(
    val dataSource: NotesDao,
    val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArchivedNotesViewModel::class.java))
            return ArchivedNotesViewModel(dataSource,application) as T

        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}