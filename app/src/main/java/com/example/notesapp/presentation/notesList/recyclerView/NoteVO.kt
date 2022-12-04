package com.example.notesapp.presentation.notesList.recyclerView

data class NoteVO(
    val id: Int = 0,
    val noteTitle: String,
    val noteLastChangedTime: String
    )
//проверить позднее формат, при необходимости изменить
// эти данные будут отображаться в RecyclerView

