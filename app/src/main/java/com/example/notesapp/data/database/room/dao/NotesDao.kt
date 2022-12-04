package com.example.notesapp.data.database.room.dao

import androidx.room.*
import com.example.notesapp.data.database.room.entities.NoteDB

@Dao
interface NotesDao {
//функции работающие с базой данных
    @Insert
    fun insertNote(note: NoteDB)
    //добавление заметок

    @Update
    fun updateNote(note: NoteDB)

    @Delete
    fun deleteNote(note: NoteDB)

    @Query("SELECT * FROM notes_table WHERE id = :noteId")
    //SQL-запрос
    //двоеточие - как переменная (в Котлине @noteId)
    fun getNote(noteId: Int): NoteDB

    @Query("SELECT * From notes_table")
    fun getAllNotes(): List<NoteDB>
}