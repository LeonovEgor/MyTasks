package ru.leonov.mytasks.ui.note

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_current_note.*
import ru.leonov.mytasks.R
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.formatedString
import java.util.*

class CurrentNoteFragment : Fragment() {

    private val EXTRA_NOTE = "NOTE"

    private var note: Note? = null
    private var currentNoteViewModel: CurrentNoteViewModel? = null
    private var navController: NavController? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentNoteViewModel = ViewModelProvider(this).get(CurrentNoteViewModel::class.java)
        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

        return inflater.inflate(R.layout.fragment_current_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        note = getNote()
        initView()
        initNavigation()
        initFab()
    }

    private fun getNote(): Note? {

        return arguments?.getParcelable(EXTRA_NOTE)
    }

    fun initView() {
        note?.let { note ->
            et_title.setText(note.title)
            et_body.setText(note.text)
            tv_status.text = note.date.formatedString()
        }

        et_title.addTextChangedListener(textChahgeListener)
        et_body.addTextChangedListener(textChahgeListener)
    }

    val textChahgeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    fun saveNote() {
        if (et_title.text == null && et_body.text == null) return

            note = note?.copy(
                    title = et_title.text.toString(),
                    text = et_body.text.toString(),
                    date = Date()
            ) ?: createNewNote()

            note?.let { currentNoteViewModel?.save(it) }
    }

    private fun createNewNote(): Note {
        return Note(UUID.randomUUID().toString(),
                et_title.text.toString(),
                et_body.text.toString())
    }

    private fun initFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.let {
            it.setImageResource(R.drawable.ic_save_black_24dp)
            it.setOnClickListener {
                saveNote()
                currentNoteViewModel?.gotoNotesList()
            }
        }
    }

    private fun initNavigation() {
        currentNoteViewModel?.gotoNotesListEvent?.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                navController?.navigate(R.id.action_note_list)
            }
        })
    }
}