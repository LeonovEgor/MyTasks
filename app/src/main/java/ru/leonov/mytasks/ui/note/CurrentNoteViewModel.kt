package ru.leonov.mytasks.ui.note

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.Event
import ru.leonov.mytasks.ui.base.BaseViewModel

@ExperimentalCoroutinesApi
class CurrentNoteViewModel(private val notesRepository: NotesRepository)
    : BaseViewModel<NoteData>() {


    private val pendingNote: Note?
        get() = getViewChannel().poll()?.note

    private val _gotoNotesListEvent = MutableLiveData<Event<Unit>>()
    val gotoNotesListEvent: LiveData<Event<Unit>>
        get() = _gotoNotesListEvent

    fun save(note: Note){
        setData(NoteData(note = note))
    }

    fun loadNote(noteId: String) {
        launch {
            try {
                val note = notesRepository.getNoteById(noteId)
                setData(NoteData(note = note))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    @VisibleForTesting
    override public fun onCleared() = save()

    fun gotoNotesList() {
        save()
        _gotoNotesListEvent.value = Event(Unit)
    }

    private fun save() {
        pendingNote?.let {save(it) }
    }

    fun delete() {
        pendingNote?.let {
            launch {
                try {
                    notesRepository.deleteNote(it.id)
                    setData(NoteData(isDeleted = true))
                } catch (e: Throwable) {
                    setError(e)
                }
            }
        }
    }
}