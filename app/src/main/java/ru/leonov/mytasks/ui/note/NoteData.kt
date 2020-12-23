package ru.leonov.mytasks.ui.note

import ru.leonov.mytasks.model.entities.Note

data class NoteData(val isDeleted: Boolean = false, val note: Note? = null)