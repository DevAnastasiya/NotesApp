package com.example.notesapp.common.utils.extensions

import androidx.fragment.app.Fragment
import androidx.room.Dao
import com.example.notesapp.common.app.App
import com.example.notesapp.data.database.room.dao.NotesDao

fun Fragment.getApp(): App {
    return (requireContext().applicationContext as App)
}

fun Fragment.getNotesDao(): NotesDao? {
    return (requireContext().applicationContext as App).notesDao
}