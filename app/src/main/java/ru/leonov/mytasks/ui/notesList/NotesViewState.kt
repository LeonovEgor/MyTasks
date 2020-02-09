package ru.leonov.mytasks.ui.notesList

import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.ui.base.BaseViewState

class NotesViewState(val notes: List<Note>? = null, error: Throwable? = null): BaseViewState<List<Note>?>(notes, error)