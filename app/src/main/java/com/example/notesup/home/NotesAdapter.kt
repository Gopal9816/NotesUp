package com.example.notesup.home


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notesup.database.Note
import com.example.notesup.databinding.HomeFragmentBinding
import com.example.notesup.databinding.NotesListItemBinding

class NotesAdapter(private val clickListener: NoteItemListener): ListAdapter<Note, RecyclerView.ViewHolder>(NotesDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent,clickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder -> {
                val data = getItem(position) as Note
                holder.bind(data)
            }
        }
    }

    fun getNoteAt(index: Int):Note{
        return getItem(index)
    }

    fun removeNote(position: Int){
        val list = currentList.toMutableList()
        list.removeAt(position)
        submitList(list)
//        notifyItemRemoved(position)
    }

    class ViewHolder private constructor(private val binding: NotesListItemBinding, private val clickListener: NoteItemListener): RecyclerView.ViewHolder(binding.root){
        fun bind(data:Note){
            binding.note = data
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup,clickListener: NoteItemListener): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NotesListItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding,clickListener)
            }
        }
    }

}

class NotesDiffCallback: DiffUtil.ItemCallback<Note>(){
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

}

class NoteItemListener(val clickListener: (Note) -> Unit){
    fun onClick(note: Note) = clickListener(note)
}