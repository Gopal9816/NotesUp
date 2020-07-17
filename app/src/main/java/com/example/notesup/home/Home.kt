package com.example.notesup.home

import android.graphics.Canvas
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.service.autofill.Validators.or
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import com.example.notesup.R
import com.example.notesup.database.NotesUpDatabase
import com.example.notesup.databinding.HomeFragmentBinding
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class Home : Fragment() {

    companion object {
        fun newInstance() = Home()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater,container,false)

        val application = requireNotNull(activity).application
        val dataSource = NotesUpDatabase.getInstance(application).notesTableDao
        val viewModelFactory = HomeViewModelFactory(dataSource,application)

        val viewModel = ViewModelProviders.of(this,viewModelFactory).get(HomeViewModel::class.java)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val noteItemListener = NoteItemListener(){
            viewModel.onNoteSelected(it)
        }
        val adapter = NotesAdapter(noteItemListener)

        binding.notesList.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(binding.notesList.context,DividerItemDecoration.VERTICAL)
        binding.notesList.addItemDecoration(dividerItemDecoration)

        val simpleItemTouchCallback = object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
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
                Snackbar.make(requireView(),"Note deleted",Snackbar.LENGTH_LONG)
                    .setAction("UNDO",View.OnClickListener {
                        adapter.submitList(viewModel.notesList.value)
                    })
                    .setActionTextColor(resources.getColor(R.color.colorPrimaryDark))
                    .addCallback(object : Snackbar.Callback(){
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)

                            if (event ==  DISMISS_EVENT_TIMEOUT or  DISMISS_EVENT_SWIPE){
                                viewModel.deleteNote(note.uid)
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
                    .addBackgroundColor(Color.RED)
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
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
        itemTouchHelper.attachToRecyclerView(binding.notesList)

        val navigationController = findNavController()

        viewModel.navigateToAddNote.observe(viewLifecycleOwner, Observer {
            if (it == true){
                navigationController.navigate(HomeDirections.actionHomeToAddNoteFragment(null))
                viewModel.onNavigateToAddNoteDone()
            }
        })

        viewModel.navigateToSelectedNote.observe(viewLifecycleOwner, Observer {
            it?.let {
                navigationController.navigate(HomeDirections.actionHomeToAddNoteFragment(it))
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
