package com.example.notesup.archive

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notesup.R

class ArchivedNotesFragment : Fragment() {

    companion object {
        fun newInstance() = ArchivedNotesFragment()
    }

    private lateinit var viewModel: ArchivedNotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.archived_notes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArchivedNotesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}