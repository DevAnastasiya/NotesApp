package com.example.notesapp.presentation.notesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
import com.example.notesapp.common.app.App
import com.example.notesapp.common.utils.extensions.getNotesDao
import com.example.notesapp.data.database.room.entities.toNoteVO
import com.example.notesapp.databinding.FragmentListBinding
import com.example.notesapp.presentation.AddNoteFragment
import com.example.notesapp.presentation.notesList.recyclerView.NotesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private var binding: FragmentListBinding? = null
    private var notesAdapter: NotesAdapter? = null

    override fun onCreateView( // обязательно переопределять при работе с Binding
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(
            inflater, container, false
        )

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListeners()
        initNotesAdapter()
    }

    private fun setClickListeners() {
        binding?.fabAddNote?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .apply {
                    addToBackStack(null).replace(R.id.fragment_container, AddNoteFragment())
                        .commit()
                }
        }
    }

    fun initNotesAdapter() { // Заполнение ресайкл вью,, соединение с ксмл-фалами и пр.
        notesAdapter = NotesAdapter()

        CoroutineScope(Dispatchers.IO).launch {
            notesAdapter?.listOfNotes =
                getNotesDao()?.getAllNotes()?.map { //getNotesDao - это extension-функция в App
                        noteDB ->
                    noteDB.toNoteVO()
                }!!.toMutableList()
        }

        notesAdapter?.onNoteClicked = { noteId -> //что делать при нажатии на заметку
            val bundle = Bundle()
            bundle.putInt("NOTE_ID", noteId)

            val fragment = AddNoteFragment()
            fragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
        }
        notesAdapter?.notesDao = getNotesDao()
        binding?.rvNotes?.adapter = notesAdapter
        binding?.rvNotes?.layoutManager = LinearLayoutManager(requireContext())
    }
}