package com.example.notesup.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesup.database.NotesDao

class HomeViewModelFactory(
    private val datasource: NotesDao,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(datasource,application) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}