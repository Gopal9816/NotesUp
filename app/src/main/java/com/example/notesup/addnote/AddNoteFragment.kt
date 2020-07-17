package com.example.notesup.addnote

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.example.notesup.R
import com.example.notesup.database.NotesUpDatabase
import com.example.notesup.databinding.AddNoteFragmentBinding

class AddNoteFragment : Fragment() {

    companion object {
        fun newInstance() = AddNoteFragment()
    }

    private lateinit var viewModel: AddNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AddNoteFragmentBinding.inflate(inflater,container,false)
        val application = requireNotNull(activity).application
        val dataSource = NotesUpDatabase.getInstance(application).notesTableDao
        val note = AddNoteFragmentArgs.fromBundle(requireArguments()).note
        val factory = AddNoteViewModelFactory(dataSource,note, application)
        val viewModel = ViewModelProviders.of(this,factory).get(AddNoteViewModel::class.java)

        binding.note = viewModel.note

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddNoteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
