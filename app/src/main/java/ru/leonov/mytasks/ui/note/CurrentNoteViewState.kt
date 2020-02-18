package ru.leonov.mytasks.ui.note

import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.ui.base.BaseViewState

class CurrentNoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<CurrentNoteViewState.Data>(data, error) {

    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}