package ru.leonov.mytasks.model.data

import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.provider.FireStoreProvider
import ru.leonov.mytasks.model.provider.RemoteDataProvider

class NotesRepository(val remoteProvider: RemoteDataProvider) {
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun getCurrentUser() = remoteProvider.getCurrentUser()
    fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)
}