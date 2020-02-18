package ru.leonov.mytasks.model.provider

import androidx.lifecycle.LiveData
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.entities.User

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getCurrentUser(): LiveData<User?>
    fun deleteNote(noteId: String): LiveData<NoteResult>
}