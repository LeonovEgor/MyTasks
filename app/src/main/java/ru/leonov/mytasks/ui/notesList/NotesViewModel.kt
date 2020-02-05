package ru.leonov.mytasks.ui.notesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.presenters.NotesViewState
import ru.leonov.mytasks.model.utils.SingleLiveEvent

class NotesViewModel : ViewModel(), INavigation {

    private val viewStateLiveData: MutableLiveData<NotesViewState> = MutableLiveData()
    private val _navigateToNotes = SingleLiveEvent<Any>()
    val navigateToDetails : LiveData<Any>
        get() = _navigateToNotes

    init {
        viewStateLiveData.value = NotesViewState(NotesRepository.getNotes())
    }

    fun getNotesViewState(): LiveData<NotesViewState> = viewStateLiveData

    override fun onNoteClick(note: Note) {
        _navigateToNotes.call(note)
    }

}