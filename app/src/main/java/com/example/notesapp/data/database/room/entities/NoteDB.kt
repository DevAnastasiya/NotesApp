package com.example.notesapp.data.database.room.entities
// это таблица базы данных
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesapp.presentation.notesList.recyclerView.NoteVO

// В data class сразу переопределена функция toString
@Entity(tableName = "notes_table")
data class NoteDB(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "note_title") val noteTitle: String,
    @ColumnInfo(name = "note_description") val noteDescription: String,
    @ColumnInfo(name = "name_last_changed_time") val noteLastChangedTime: String
)
//@PrimaryKey, @ColumnInfo - это аннотации, name =  название колонки в таблице с БД

// Mapping, mapper - процесс создания одного типа данных из другого
fun NoteDB.toNoteVO(): NoteVO {
    return NoteVO(id = id, noteTitle = noteTitle, noteLastChangedTime = noteLastChangedTime)

}