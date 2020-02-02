package ru.leonov.mytasks.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.presenters.NotesViewState

class NotesViewModel : ViewModel() {

    private val viewStateLiveData: MutableLiveData<NotesViewState> = MutableLiveData()

    init {
        viewStateLiveData.value = NotesViewState(NotesRepository.notes)
    }

    fun getNotesViewState(): LiveData<NotesViewState> = viewStateLiveData

}