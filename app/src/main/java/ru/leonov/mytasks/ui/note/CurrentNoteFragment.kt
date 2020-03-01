package ru.leonov.mytasks.ui.note

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_current_note.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.sharedViewModel
import ru.leonov.mytasks.R
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.getColorInt
import ru.leonov.mytasks.model.utils.getDateString
import ru.leonov.mytasks.ui.base.BaseFragment
import java.util.*

@ExperimentalCoroutinesApi
class CurrentNoteFragment : BaseFragment<NoteData>() {

    private val NOTE_ID = "NOTE"

    private var note: Note? = null
    var color = Note.Color.WHITE

    private lateinit var mnuDelete: MenuItem
    private lateinit var mnuColor: MenuItem

    override val layoutRes = R.layout.fragment_current_note
    override val viewModel: CurrentNoteViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true);

        loadNote()
        initNavigation()
        initFab()
    }

    private fun loadNote() {
        val noteId = arguments?.getString(NOTE_ID)

        noteId?.let {
            viewModel.loadNote(it)
        } ?: let {
            note = null
            initView()
        }
    }

    private fun initFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.let {
            it.setImageResource(R.drawable.ic_save_black_24dp)
            it.setOnClickListener {
                saveNote()
                viewModel.gotoNotesList()
            }
        }
    }

    private fun initNavigation() {
        viewModel.gotoNotesListEvent.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                activity?.onBackPressed()
            }
        })
    }

    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = saveNote()
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun setEditListener() {
        et_title.addTextChangedListener(textChangeListener)
        et_body.addTextChangedListener(textChangeListener)
    }

    private fun removeEditListener() {
        et_title.removeTextChangedListener(textChangeListener)
        et_body.removeTextChangedListener(textChangeListener)
    }

    fun saveNote() {
        if (et_title.text == null || et_title.text!!.length < 3) return

            note = note?.copy(
                    title = et_title.text.toString(),
                    text = et_body.text.toString(),
                    date = Date(),
                    color = color
            ) ?: Note(UUID.randomUUID().toString(),
                    et_title.text.toString(),
                    et_body.text.toString(),
                    date = Date(),
                    color = color)

            note?.let { viewModel.save(it) }
    }

    override fun renderData(data: NoteData) {
        if (data.isDeleted) {
            viewModel.gotoNotesList()
            return
        }

        this.note = data.note

        initView()
    }

    private fun deleteNote() {
        activity?.alert {
            messageResource = R.string.note_delete_message
            negativeButton(R.string.note_delete_cancel) { dialog -> dialog.dismiss() }
            positiveButton(R.string.note_delete_ok) { viewModel.delete() }
        }?.show()
    }


    fun initView() {
        note?.let { note ->
            removeEditListener()
            if(et_title.text.toString() != note.title) et_title.setText(note.title)
            if(et_body.text.toString() != note.text) et_body.setText(note.text)
            color = note.color;
            tv_status.text = note.date.getDateString()
        }?: let {
            tv_status.text = getString(R.string.new_note)
        }

        setEditListener()

        colorPicker.onColorClickListener = { selectedColor ->
            context?.let { checkedContext ->
                et_title.setBackgroundColor(selectedColor.getColorInt(checkedContext))
                et_body.setBackgroundColor(selectedColor.getColorInt(checkedContext))
                color = selectedColor
                saveNote()
                colorPicker.close()
            }
        }
    }

    override fun showError(error: String) {
        tv_status.text = error
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    //region [menu operation]

    override fun onPause() {
        super.onPause()
        mnuDelete.isVisible = false;
        mnuColor.isVisible = false;
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        mnuDelete = menu.findItem(R.id.delete)
        mnuColor = menu.findItem(R.id.palette)
        mnuDelete.isVisible = true;
        mnuColor.isVisible = true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when(item.itemId) {
                android.R.id.home -> viewModel.gotoNotesList().let { true }
                R.id.palette -> togglePalette().let { true }
                R.id.delete -> deleteNote().let { true }
                else -> false
            }

    //endregion
}