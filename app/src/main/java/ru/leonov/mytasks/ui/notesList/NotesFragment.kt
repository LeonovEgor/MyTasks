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
import kotlinx.android.synthetic.main.fragment_notes_list.*
import ru.leonov.mytasks.R

class NotesFragment : Fragment(), INavigation {

    private var notesViewModel: NotesViewModel? = null
    private lateinit var adapter: NotesRVAdapter
    private var navController: NavController? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        // получаем навигационный контроллер по идентификатору навигацинного фрагмента из активити
        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_notes.layoutManager = GridLayoutManager(activity, 2)
        adapter = NotesRVAdapter(note -> notesViewModel. )
        rv_notes.adapter = adapter

        notesViewModel?.getNotesViewState()?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.notes = it.notes
            }
        })
    }

    override fun onNoteClick(Note note) {
        navController?.navigate(R.id.action_current_note);
    }
}