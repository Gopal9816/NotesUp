package com.example.notesup.archive

import android.graphics.Canvas
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notesup.R
import com.example.notesup.database.NotesUpDatabase
import com.example.notesup.databinding.ArchivedNotesFragmentBinding
import com.example.notesup.home.HomeDirections
import com.example.notesup.home.NoteItemListener
import com.example.notesup.home.NotesAdapter
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class ArchivedNotesFragment : Fragment() {

    companion object {
        fun newInstance() = ArchivedNotesFragment()
    }

    private lateinit var viewModel: ArchivedNotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ArchivedNotesFragmentBinding.inflate(inflater,container,false)
        val application =  requireNotNull(activity).application
        val dataSource = NotesUpDatabase.getInstance(application).notesTableDao
        val viewModelFactory = ArchivedNotesViewModelFactory(dataSource, application)

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(ArchivedNotesViewModel::class.java)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val noteItemListener = NoteItemListener(){
            viewModel.onNoteSelected(it)
        }
        val adapter = NotesAdapter(noteItemListener)

        binding.archivedNotesList.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(binding.archivedNotesList.context,
            DividerItemDecoration.VERTICAL)
        binding.archivedNotesList.addItemDecoration(dividerItemDecoration)

        val simpleItemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = adapter.getNoteAt(viewHolder.adapterPosition)
                adapter.removeNote(viewHolder.adapterPosition)

                lateinit var snackbarMessage: String

                if (direction == ItemTouchHelper.RIGHT)
                    snackbarMessage = "Note deleted"
                else if (direction == ItemTouchHelper.LEFT)
                    snackbarMessage = "Note unarchived"

                Snackbar.make(requireView(),snackbarMessage, Snackbar.LENGTH_LONG)
                    .setAction("UNDO",View.OnClickListener {
                        adapter.submitList(viewModel.notesList.value)
                    })
                    .addCallback(object : Snackbar.Callback(){
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)

                            if (event ==  DISMISS_EVENT_TIMEOUT or  DISMISS_EVENT_SWIPE){
                                if (direction == ItemTouchHelper.RIGHT)
                                    viewModel.deleteNote(note.uid)
                                else if (direction == ItemTouchHelper.LEFT)
                                    viewModel.unArchiveNote(note)
                            }
                        }
                    })
                    .show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeRightBackgroundColor(Color.RED)
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                    .setSwipeRightLabelColor(Color.WHITE)
                    .addSwipeRightLabel("DELETE")
                    .addSwipeLeftBackgroundColor(Color.GREEN)
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_archive_24)
                    .addSwipeLeftLabel("UNARCHIVE")
                    .setSwipeLeftLabelColor(Color.WHITE)
                    .create()
                    .decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.archivedNotesList)


        val navigationController = findNavController()
        viewModel.navigateToSelectedNote.observe(viewLifecycleOwner, Observer {
            it?.let {
                navigationController.navigate(ArchivedNotesFragmentDirections.actionArchivedNotesFragmentToAddNoteFragment(it))
                viewModel.onNavigateToSelectedNoteDone()
            }
        })

        viewModel.notesList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        return binding.root
    }

}