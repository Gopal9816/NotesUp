package com.example.notesup.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Entity(tableName = "notes_table")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0L,
    var title: String = "Untitled",
    var description: String?,
    @TypeConverters(Convertors::class)
    var timestamp: Date?,
    var archived: Boolean = false
): Parcelable