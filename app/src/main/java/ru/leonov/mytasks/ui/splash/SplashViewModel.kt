package ru.leonov.mytasks.ui.splash

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.leonov.mytasks.model.data.NoAuthException
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.ui.base.BaseViewModel

@ExperimentalCoroutinesApi
class SplashViewModel(private val notesRepository: NotesRepository): BaseViewModel<Boolean?>() {

    fun requestUser() {
        launch {
            notesRepository.getCurrentUser()?.let {
                    setData(true)
                } ?: setError(NoAuthException())
        }
    }
}