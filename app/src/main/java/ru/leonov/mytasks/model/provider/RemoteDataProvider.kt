package ru.leonov.mytasks.model.provider

import kotlinx.coroutines.channels.ReceiveChannel
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.entities.User

interface RemoteDataProvider {
    fun subscribeToAllNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note): Note
    suspend fun getCurrentUser(): User?
    suspend fun deleteNote(noteId: String)
}