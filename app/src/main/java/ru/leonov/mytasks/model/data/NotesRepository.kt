package ru.leonov.mytasks.model.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.leonov.mytasks.model.entities.Color
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.provider.FireStoreProvider
import ru.leonov.mytasks.model.provider.RemoteDataProvider
import ru.leonov.mytasks.model.utils.getCurrentDateTime
import java.util.*

object NotesRepository {
    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getNotes() = remoteProvider.subsrcibeToAllNotes()

}