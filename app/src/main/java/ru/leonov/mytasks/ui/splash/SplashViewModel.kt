package ru.leonov.mytasks.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.leonov.mytasks.model.data.NoAuthException
import ru.leonov.mytasks.model.data.NotesRepository


class SplashViewModel(private val notesRepository: NotesRepository): ViewModel() {
    private val viewStateLiveData = MutableLiveData<SplashViewState>()
    fun getViewState(): LiveData<SplashViewState> = viewStateLiveData

    fun requestUser() {
        notesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = it?.let {
                SplashViewState(authenticated = true)
            } ?: let {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}