package ru.leonov.mytasks.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_current_note.*
import ru.leonov.mytasks.R
import ru.leonov.mytasks.model.entities.Color
import ru.leonov.mytasks.model.entities.Note
import java.util.*

class CurrentNoteFragment : Fragment() {

    companion object {
        private val EXTRA_NOTE = CurrentNoteFragment::class.java.name + "extra.NOTE"

        fun start(context: Context, note: Note? = null) {
            val intent = Intent(context, CurrentNoteFragment::class.java)
            intent.putExtra(EXTRA_NOTE, note)
            context.startActivity(intent)
        }
    }

    private var note: Note? = null
    private var currentNoteViewModel: CurrentNoteViewModel? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        note = getNote()

        currentNoteViewModel = ViewModelProvider(this).get(CurrentNoteViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_current_note, container, false)
        initView()

        return root
    }

    private fun getNote(): Note? {
        return activity?.intent?.getParcelableExtra(EXTRA_NOTE)
    }

    val textChahgeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    fun initView() {
        note?.let { note ->
            et_title.setText(note.title)
            et_body.setText(note.text)
            val color = when (note.color) {
                Color.WHITE -> R.color.white
                Color.YELLOW -> R.color.yellow
                Color.GREEN -> R.color.green
                Color.BLUE -> R.color.blue
                Color.RED -> R.color.red
                Color.VIOLET -> R.color.violet
                Color.PINK -> R.color.pink
            }

            activity?.baseContext?.let {
                ContextCompat.getColor(it, color) }?.let { toolbar.setBackgroundColor(it)
            }
        }

        et_title.addTextChangedListener(textChahgeListener)
        et_body.addTextChangedListener(textChahgeListener)
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



}