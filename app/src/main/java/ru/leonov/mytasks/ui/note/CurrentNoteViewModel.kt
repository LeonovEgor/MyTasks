package ru.leonov.mytasks.ui.note

import androidx.lifecycle.ViewModel
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.entities.Note

class CurrentNoteViewModel : ViewModel() {

    private var pendingNote: Note? = null

    fun save(note: Note){
        pendingNote = note
    }

    override fun onCleared(){
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
    }
}