package ru.leonov.mytasks.ui.notesList

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.Event
import ru.leonov.mytasks.ui.base.BaseViewModel

class NotesViewModel(private val notesRepository: NotesRepository) : BaseViewModel<List<Note>?, NotesViewState>() {

    private val allNotes = notesRepository.getNotes()

    @Suppress("UNCHECKED_CAST")
    private val notesObserver = {noteResult: NoteResult ->
        noteResult.let {
            when (noteResult) {
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value = NotesViewState(notes = noteResult.data as? List<Note>)
                }
                is NoteResult.Error -> {
                    viewStateLiveData.value = NotesViewState(error = noteResult.error)
                }
            }
        }
    }

    init {
        viewStateLiveData.value = NotesViewState()
        allNotes.observeForever(notesObserver)
    }

    @VisibleForTesting
    override public fun onCleared() {
        allNotes.removeObserver(notesObserver)
        super.onCleared()
    }

    private val _openNoteEvent = MutableLiveData<Event<Note>>()
    val openNoteEvent: LiveData<Event<Note>>
        get() = _openNoteEvent

    private val _openNewNoteEvent = MutableLiveData<Event<Unit>>()
    val openNewNoteEvent: LiveData<Event<Unit>>
        get() = _openNewNoteEvent

    fun onNoteClick(note: Note) {
        _openNoteEvent.value = Event(note)
    }

    fun onNewNoteClick() {
        _openNewNoteEvent.value = Event(Unit)
    }
}