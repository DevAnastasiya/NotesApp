package com.example.notesapp.common.app

import android.app.Application
import com.example.notesapp.data.database.room.dao.NotesDao
import com.example.notesapp.data.database.room.database.AppDatabase

class App : Application() {
// класс App представляет наше приложение, т.к. наследуется от Application
// класс App становится синглтоном после наследования,
// его обязательно нужно указать в манифесте и он должен быть только один

    var database: AppDatabase? = null
    var notesDao: NotesDao? = null

    override fun onCreate() {
        database = AppDatabase.getDatabase(this)
        //здесь создается единая база данных
        notesDao = database?.getNotesDao()

        super.onCreate()
    }
}