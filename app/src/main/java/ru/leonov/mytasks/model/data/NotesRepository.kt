package ru.leonov.mytasks.model.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.getCurrentDateTime
import java.util.*

object NotesRepository {
    private val notesLiveData = MutableLiveData<List<Note>>()

    private val notes: MutableList<Note> = mutableListOf(
            Note(
                    UUID.randomUUID().toString(),
                    "Заметка 1",
                    "Текст заметки 1. Не очень длинный, но интересный",
                    getCurrentDateTime(),
                    0xfff06292.toInt()
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Заметка 2",
                    "Текст заметки 2. Не очень длинный, но интересный",
                    getCurrentDateTime(),
                    0xff9575cd.toInt()
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Заметка 3",
                    "Текст заметки 3. Не очень длинный, но интересный",
                    getCurrentDateTime(),
                    0xff64b5f6.toInt()
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Заметка 4",
                    "Текст заметки 4. Не очень длинный, но интересный",
                    getCurrentDateTime(),
                    0xff4db6ac.toInt()
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Заметка 5",
                    "Текст заметки 5. Не очень длинный, но интересный",
                    getCurrentDateTime(),
                    0xffb2ff59.toInt()
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Заметка 6",
                    "Текст заметки 6. Не очень длинный, но интересный",
                    getCurrentDateTime(),
                    0xffffeb3b.toInt()
            )
    )

    init {
        notesLiveData.value = notes
    }

    fun saveNote(note: Note){
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note){
        for(i in notes.indices){
            if(notes[i] == note){
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }

    fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

}