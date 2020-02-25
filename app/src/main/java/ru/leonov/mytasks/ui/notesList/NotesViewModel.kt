package ru.leonov.mytasks.ui.notesList

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.Event
import ru.leonov.mytasks.ui.base.BaseViewModel

@ExperimentalCoroutinesApi
class NotesViewModel(notesRepository: NotesRepository) : BaseViewModel<List<Note>?>() {

    private val notesReceiveChannel = notesRepository.getNotes() //ReceiveChannel<>

    init {
        @Suppress("UNCHECKED_CAST")
        launch {
            notesReceiveChannel.consumeEach { noteResult ->
                when (noteResult) {
                    is NoteResult.Success<*> -> setData(noteResult.data as? List<Note>)
                    is NoteResult.Error -> setError(noteResult.error)
                }
            }
        }
    }

    @VisibleForTesting
    override public fun onCleared() {
        notesReceiveChannel.cancel()
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