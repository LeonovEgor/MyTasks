package ru.leonov.mytasks.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import ru.leonov.mytasks.R

class NotesFragment : Fragment() {

    private var notesViewModel: NotesViewModel? = null
    private lateinit var adapter: NotesRVAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_notes.layoutManager = GridLayoutManager(activity, 2)
        adapter = NotesRVAdapter()
        rv_notes.adapter = adapter

        notesViewModel?.getNotesViewState()?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.notes = it.notes
            }
        })
    }
}