package ru.leonov.mytasks.ui.notesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel
import ru.leonov.mytasks.R
import ru.leonov.mytasks.databinding.FragmentNotesListBinding
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.ui.base.BaseFragment

@ExperimentalCoroutinesApi
class NotesFragment : BaseFragment<List<Note>?>() {

    private val noteId = "NOTE"

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private var navController: NavController? = null

    //override val layoutRes = R.layout.fragment_notes_list

    override val viewModel: NotesViewModel by sharedViewModel()

    private lateinit var adapter: NotesRVAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initNavigation()
        initFab()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        binding.rvNotes.layoutManager = GridLayoutManager(activity, 2)

        adapter = NotesRVAdapter {note -> viewModel.onNoteClick(note) }
        binding.rvNotes.adapter = adapter
    }

    private fun initNavigation() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        viewModel.openNoteEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {note ->
                val bundle = Bundle()
                bundle.putString(noteId, note.id)
                navController?.navigate(R.id.action_current_note, bundle)
            }
        })

        viewModel.openNewNoteEvent.observe(viewLifecycleOwner, { event ->
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
            binding.tvError.text = ""
            binding.tvError.visibility = View.GONE
        }
    }


    override fun showError(error: String) {
        binding.tvError.visibility = View.VISIBLE
        binding.tvError.text = error
    }

}