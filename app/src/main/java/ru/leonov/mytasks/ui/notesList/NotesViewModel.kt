package ru.leonov.mytasks.ui.notesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.presenters.NotesViewState
import ru.leonov.mytasks.model.utils.Event

class NotesViewModel : ViewModel() {

    private val viewStateLiveData: MutableLiveData<NotesViewState> = MutableLiveData()

    private val _openNoteEvent = MutableLiveData<Event<Note>>()
    val openNoteEvent: LiveData<Event<Note>>
        get() = _openNoteEvent

    private val _openNewNoteEvent = MutableLiveData<Event<Unit>>()
    val openNewNoteEvent: LiveData<Event<Unit>>
        get() = _openNewNoteEvent

    init {
        NotesRepository.getNotes().observeForever {notes ->
            viewStateLiveData.value = viewStateLiveData.value?.copy(notes = notes) ?: NotesViewState(notes)
        }
    }

    fun getNotesViewState(): LiveData<NotesViewState> = viewStateLiveData

    fun onNoteClick(note: Note) {
        _openNoteEvent.value = Event(note)
    }

    fun onNewNoteClick() {
        _openNewNoteEvent.value = Event(Unit)
    }

}