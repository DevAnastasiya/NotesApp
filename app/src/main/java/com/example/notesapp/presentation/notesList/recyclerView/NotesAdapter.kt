package com.example.notesapp.presentation.notesList.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.data.database.room.dao.NotesDao
import com.example.notesapp.data.database.room.entities.NoteDB
import com.example.notesapp.databinding.NotesAdapterViewHolderBinding
import kotlinx.coroutines.*

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    var listOfNotes: MutableList<NoteVO> = mutableListOf() // делаем изменяемый список
    var notesDao: NotesDao? = null
    var onNoteClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            NotesAdapterViewHolderBinding
                .inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), notesDao, onNoteClicked
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
// связывает эл-т списка с его данными (тело в fun bind ниже)
        val note = listOfNotes[position]

        holder.bind(note)
        CoroutineScope(Dispatchers.IO).launch {
            notesDao?.getNote(note.id)?.let { noteDB ->
                withContext(Dispatchers.Main) {
                    holder.setOnClickListeners(noteDB) { // удаление из БД
                        deleteNoteFromList(note)
                    } // "эл-т на определенной позиции удален" (предупреждает RV)
                }
            }
        }
    }
        // DRY (don't repeat yourself)

        override fun getItemCount(): Int {
            return listOfNotes.size
        }

        private fun deleteNoteFromList(noteVO: NoteVO) {
            val indexToDelete =
                listOfNotes.indexOfFirst { // функция пробегается по листу и проверяет условие далее
                        currentNote ->
                    currentNote.id == noteVO.id
                }
            if (indexToDelete != -1) {
                listOfNotes.removeAt(indexToDelete)
                notifyItemRemoved(indexToDelete)
            }
        }

        class NotesViewHolder(
            private val binding: NotesAdapterViewHolderBinding,
            private val notesDao: NotesDao?,
            private val onNoteClicked: ((Int) -> Unit)?
        )
        //принимает дизайн (xml-файл)
            : RecyclerView.ViewHolder(binding.root) {

            fun bind(noteVO: NoteVO) {
                binding.tvNoteTitle.text = noteVO.noteTitle
                binding.tvNoteDescription.text = noteVO.noteLastChangedTime
            }

            fun setOnClickListeners(
                currentNote: NoteDB,
                onDeleteNoteClicked: () -> Unit
            ) {
                binding.root.setOnClickListener { //вызывает лямбду если она не null
                    onNoteClicked?.let { onNoteClicked -> onNoteClicked(currentNote.id) }
                }

                binding.ivDeleteNote.setOnClickListener() {
                    CoroutineScope(Dispatchers.IO).launch {
                        notesDao?.deleteNote(currentNote)
                    }
                    onDeleteNoteClicked()
                }
            }
        }
    }