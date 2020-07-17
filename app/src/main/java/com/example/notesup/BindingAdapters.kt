package com.example.notesup

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.notesup.database.Note
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("listEmptyCheck")
fun listEmptyCheck(statusTextView: TextView, list: List<Note>?){
    if (list == null){
        statusTextView.visibility = View.VISIBLE
    }
    else{
        statusTextView.visibility = View.GONE
    }
}

@BindingAdapter("formatDate")
fun formatDate(dateTextView: TextView, date: Date){
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    dateTextView.text = formatter.format(date)

}

@BindingAdapter("editedTimestamp")
fun editedTimestamp(textView: TextView,date: Date){
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    textView.text = "Edited ${formatter.format(date)}"
}

@BindingAdapter("previewText")
fun previewText(textView: TextView,text:String?){
    text?.let {
        var len = text.length
        if (len > 40)
            len = 40
        val preview = text.substring(0,len).substringBefore('\n').plus("...")
        textView.text = preview
    }
}