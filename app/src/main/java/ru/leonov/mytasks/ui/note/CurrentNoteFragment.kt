package ru.leonov.mytasks.ui.note

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.sharedViewModel
import ru.leonov.mytasks.R
import ru.leonov.mytasks.databinding.FragmentCurrentNoteBinding
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.getColorInt
import ru.leonov.mytasks.model.utils.getDateString
import ru.leonov.mytasks.ui.base.BaseFragment
import java.util.*

@ExperimentalCoroutinesApi
class CurrentNoteFragment : BaseFragment<NoteData>() {

    private val noteId = "NOTE"

    private var _binding: FragmentCurrentNoteBinding? = null
    private val binding get() = _binding!!

    private var note: Note? = null
    var color = Note.Color.WHITE

    private lateinit var mnuDelete: MenuItem
    private lateinit var mnuColor: MenuItem

    override val viewModel: CurrentNoteViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCurrentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        loadNote()
        initNavigation()
        initFab()
    }

    private fun loadNote() {
        val noteId = arguments?.getString(noteId)

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
        viewModel.gotoNotesListEvent.observe(viewLifecycleOwner, { event ->
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
        binding.etTitle.addTextChangedListener(textChangeListener)
        binding.etBody.addTextChangedListener(textChangeListener)
    }

    private fun removeEditListener() {
        binding.etTitle.removeTextChangedListener(textChangeListener)
        binding.etBody.removeTextChangedListener(textChangeListener)
    }

    fun saveNote() {
        if (binding.etTitle.text == null || binding.etTitle.text!!.length < 3) return

            note = note?.copy(
                    title = binding.etTitle.text.toString(),
                    text = binding.etBody.text.toString(),
                    date = Date(),
                    color = color
            ) ?: Note(UUID.randomUUID().toString(),
                    binding.etTitle.text.toString(),
                    binding.etBody.text.toString(),
                    date = Date(),
                    color = color)

            note?.let { viewModel.save(it) }
    }

    private fun deleteNote() {
        activity?.alert {
            messageResource = R.string.note_delete_message
            negativeButton(R.string.note_delete_cancel) { dialog -> dialog.dismiss() }
            positiveButton(R.string.note_delete_ok) { viewModel.delete() }
        }?.show()
    }

    override fun renderData(data: NoteData) {
        if (data.isDeleted) {
            viewModel.gotoNotesList()
            return
        }

        this.note = data.note

        initView()
    }

    private fun initView() {
        note?.let { note ->
            removeEditListener()
            if(binding.etTitle.text.toString() != note.title) binding.etTitle.setText(note.title)
            if(binding.etBody.text.toString() != note.text) binding.etBody.setText(note.text)
            color = note.color
            binding.tvStatus.text = note.date.getDateString()
        }?: let {
            binding.tvStatus.text = getString(R.string.new_note)
        }

        setEditListener()

        binding.colorPicker.onColorClickListener = { selectedColor ->
            context?.let { checkedContext ->
                binding.etTitle.setBackgroundColor(selectedColor.getColorInt(checkedContext))
                binding.etBody.setBackgroundColor(selectedColor.getColorInt(checkedContext))
                color = selectedColor
                saveNote()
                binding.colorPicker.close()
            }
        }
    }

    override fun showError(error: String) {
        binding.tvStatus.text = error
    }

    private fun togglePalette() {
        if (binding.colorPicker.isOpen) {
            binding.colorPicker.close()
        } else {
            binding.colorPicker.open()
        }
    }

    //region [menu operation]

    override fun onPause() {
        super.onPause()
        mnuDelete.isVisible = false
        mnuColor.isVisible = false
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        mnuDelete = menu.findItem(R.id.delete)
        mnuColor = menu.findItem(R.id.palette)
        mnuDelete.isVisible = true
        mnuColor.isVisible = true
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