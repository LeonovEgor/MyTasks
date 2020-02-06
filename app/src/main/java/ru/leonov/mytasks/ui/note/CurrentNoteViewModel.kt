package ru.leonov.mytasks.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.Event

class CurrentNoteViewModel : ViewModel() {

    private var pendingNote: Note? = null

    private val _gotoNotesListEvent = MutableLiveData<Event<Unit>>()
    val gotoNotesListEvent: LiveData<Event<Unit>>
        get() = _gotoNotesListEvent

    fun save(note: Note){
        pendingNote = note
    }

    override fun onCleared() = save()

    fun gotoNotesList() {
        save()
        _gotoNotesListEvent.value = Event(Unit)
    }

    private fun save() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
    }
}