package ru.leonov.mytasks.ui.splash

import ru.leonov.mytasks.model.data.NoAuthException
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.ui.base.BaseViewModel


class SplashViewModel() : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        NotesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = it?.let {
                SplashViewState(authenticated = true)
            } ?: let {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}