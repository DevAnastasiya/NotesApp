package com.example.notesapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.example.notesapp.R
import com.example.notesapp.common.utils.extensions.getNotesDao
import com.example.notesapp.data.database.room.entities.NoteDB
import com.example.notesapp.databinding.FragmentAddNoteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer

class AddNoteFragment : Fragment() {

    private var binding: FragmentAddNoteBinding? = null
    private var isNoteCreated: Boolean = false
    private var currentNote: NoteDB? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding?.root //root - возвращаемый тип функции, т.е. View? выше
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments?.getInt("NOTE_ID") != null) { //если фрагменты не пустые - то навигация совершена через заметки (т.е. заметка существует)
            isNoteCreated = true //значит заметка создана

            val noteDeferred =
                CoroutineScope(Dispatchers.IO).async { // deferred - возвращаемый асинком тип, данные не выжидаются
                    getNotesDao()?.getNote(arguments?.getInt("NOTE_ID")!!) // запрос в БД
                }

            CoroutineScope(Dispatchers.Main).launch {
                currentNote = noteDeferred.await()

                loadCurrentNoteData()
                binding?.btnSaveNote?.text =
                    getString(R.string.btn_update_note_text_add_note_fragment)
            }
        }

        setOnClickListeners()
    }

    private fun loadCurrentNoteData() {
        binding?.etNoteTitle?.setText(currentNote?.noteTitle)
        binding?.etNoteDescription?.setText(currentNote?.noteDescription)
    }

    private fun setOnClickListeners() {
        binding?.btnSaveNote?.setOnClickListener {
            val noteTitle = binding?.etNoteTitle?.text.toString()
            val noteDescription = binding?.etNoteDescription?.text.toString()

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault())
            val currentDate = sdf.format(Date())

            CoroutineScope(Dispatchers.IO).launch {
                val note = NoteDB(
                    id = currentNote?.id ?: 0,
                    noteTitle = noteTitle,
                    noteDescription = noteDescription,
                    noteLastChangedTime = currentDate
                )
                //breakpoints - точки остановки, приложение работает до них
                // Log.e("CHECKING", isNoteCreated.toString()) // логирование данных, продвинутый println
                // проверяет данные и выводит их в logcat, их можно искать по ключевому слову (CHECKING)
                if (isNoteCreated) {
                    getNotesDao()?.updateNote(note)
                } else {
                    getNotesDao()?.insertNote(note)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ListFragment()).commit()
        }
    }
}