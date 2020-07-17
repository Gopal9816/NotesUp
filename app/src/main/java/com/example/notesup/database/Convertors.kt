package com.example.notesup.database

import androidx.room.TypeConverter
import java.util.Date

class Convertors{
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date?{
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return date?.time
    }
}