package ru.leonov.mytasks

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.startKoin
import ru.leonov.mytasks.model.di.appModule
import ru.leonov.mytasks.model.di.currentNoteModule
import ru.leonov.mytasks.model.di.notesModule
import ru.leonov.mytasks.model.di.splashModule

class App: Application() {
    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule, splashModule, notesModule, currentNoteModule))
    }
}