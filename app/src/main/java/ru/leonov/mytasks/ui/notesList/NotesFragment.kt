package ru.leonov.mytasks.ui.notesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_notes_list.*
import ru.leonov.mytasks.R

class NotesFragment : Fragment() {
    private val EXTRA_NOTE = "NOTE"

    private var notesViewModel: NotesViewModel? = null
    private lateinit var adapter: NotesRVAdapter
    private var navController: NavController? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initNavigation()
        initFab()
    }

    private fun initRecyclerView() {
        rv_notes.layoutManager = GridLayoutManager(activity, 2)

        adapter = NotesRVAdapter {note -> notesViewModel?.onNoteClick(note) }
        rv_notes.adapter = adapter

        notesViewModel?.getNotesViewState()?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.notes = it.notes
            }
        })
    }

    private fun initNavigation() {
        notesViewModel?.openNoteEvent?.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {note ->
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_NOTE, note)
                navController?.navigate(R.id.action_current_note, bundle)
            }
        })

        notesViewModel?.openNewNoteEvent?.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                navController?.navigate(R.id.action_current_note)
            }
        })

    }

    private fun initFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.let {
            it.setImageResource(R.drawable.ic_add_black_24dp)
            it.setOnClickListener {
                notesViewModel?.onNewNoteClick()
            }
        }
    }
}