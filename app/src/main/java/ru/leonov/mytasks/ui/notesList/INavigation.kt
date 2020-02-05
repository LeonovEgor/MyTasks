package ru.leonov.mytasks.ui.notesList

import ru.leonov.mytasks.model.entities.Note

interface INavigation {
    fun onNoteClick(note: Note)

    interface View {
        fun navigateToSelectedNote(note: Note)
    }
}