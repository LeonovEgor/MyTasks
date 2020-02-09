package ru.leonov.mytasks.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.Event
import ru.leonov.mytasks.ui.base.BaseViewModel

class CurrentNoteViewModel : BaseViewModel<Note?, CurrentNoteViewState>() {


    private var pendingNote: Note? = null

    private val _gotoNotesListEvent = MutableLiveData<Event<Unit>>()
    val gotoNotesListEvent: LiveData<Event<Unit>>
        get() = _gotoNotesListEvent

    init {
        viewStateLiveData.value = CurrentNoteViewState()
    }

    fun save(note: Note){
        pendingNote = note
    }

    fun loadNote(noteId: String) {
        NotesRepository.getNoteById(noteId).observeForever {noteResult->
            noteResult?.let {
                    when (noteResult) {
                        is NoteResult.Success<*> -> {
                            viewStateLiveData.value = CurrentNoteViewState(note = noteResult.data as Note)
                        }
                        is NoteResult.Error -> {
                            viewStateLiveData.value = CurrentNoteViewState(error = noteResult.error)
                        }
                    }
                }
            }
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