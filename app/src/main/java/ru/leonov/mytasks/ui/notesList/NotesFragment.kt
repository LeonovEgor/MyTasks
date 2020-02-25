package ru.leonov.mytasks.ui.notesList

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_notes_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel
import ru.leonov.mytasks.R
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.ui.base.BaseFragment

@ExperimentalCoroutinesApi
class NotesFragment : BaseFragment<List<Note>?>() {

    private val NOTE_ID = "NOTE"

    private var navController: NavController? = null

    override val layoutRes = R.layout.fragment_notes_list

    override val viewModel: NotesViewModel by sharedViewModel()

    private lateinit var adapter: NotesRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initNavigation()
        initFab()
    }

    private fun initRecyclerView() {
        rv_notes.layoutManager = GridLayoutManager(activity, 2)

        adapter = NotesRVAdapter {note -> viewModel.onNoteClick(note) }
        rv_notes.adapter = adapter
    }

    private fun initNavigation() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        viewModel.openNoteEvent.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {note ->
                val bundle = Bundle()
                bundle.putString(NOTE_ID, note.id)
                navController?.navigate(R.id.action_current_note, bundle)
            }
        })

        viewModel.openNewNoteEvent.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                navController?.navigate(R.id.action_current_note)
            }
        })
    }

    private fun initFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.let {
            it.setImageResource(R.drawable.ic_add_black_24dp)
            it.setOnClickListener {
                viewModel.onNewNoteClick()
            }
        }
    }

    override fun renderData(data: List<Note>?) {
        data?.let {
            adapter.notes = it
            tv_error.text = ""
            tv_error.visibility = View.GONE
        }
    }


    override fun showError(error: String) {
        tv_error.visibility = View.VISIBLE
        tv_error.text = error
    }
}