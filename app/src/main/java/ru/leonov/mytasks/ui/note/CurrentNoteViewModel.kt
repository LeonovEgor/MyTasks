package ru.leonov.mytasks.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.Event
import ru.leonov.mytasks.ui.base.BaseViewModel

class CurrentNoteViewModel(private val notesRepository: NotesRepository)
    : BaseViewModel<CurrentNoteViewState.Data, CurrentNoteViewState>() {


    private val pendingNote: Note?
        get() = viewStateLiveData.value?.data?.note

    private val _gotoNotesListEvent = MutableLiveData<Event<Unit>>()
    val gotoNotesListEvent: LiveData<Event<Unit>>
        get() = _gotoNotesListEvent



    fun save(note: Note){
        viewStateLiveData.value = CurrentNoteViewState(CurrentNoteViewState.Data(note = note))
    }

    fun loadNote(noteId: String) {
        notesRepository.getNoteById(noteId).observeForever { result ->
            result?.let {
                viewStateLiveData.value = when (result) {
                    is NoteResult.Success<*> -> CurrentNoteViewState(CurrentNoteViewState.Data(note = result.data as Note))
                    is NoteResult.Error -> CurrentNoteViewState(error = result.error)
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
            notesRepository.saveNote(it)
        }
    }

    fun delete() {
        pendingNote?.let {
            notesRepository.deleteNote(it.id).observeForever { result ->
                viewStateLiveData.value = when (result) {
                    is NoteResult.Success<*> -> CurrentNoteViewState(CurrentNoteViewState.Data(isDeleted = true))
                    is NoteResult.Error -> CurrentNoteViewState(error = result.error)
                }
            }
        }
    }
}