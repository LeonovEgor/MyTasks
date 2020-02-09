package ru.leonov.mytasks.ui.note

import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.ui.base.BaseViewState

class CurrentNoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)