package ru.leonov.mytasks

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.startKoin
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.di.appModule
import ru.leonov.mytasks.model.di.currentNoteModule
import ru.leonov.mytasks.model.di.notesModule

import ru.leonov.mytasks.model.di.splashModule
import ru.leonov.mytasks.model.provider.FireStoreProvider
import ru.leonov.mytasks.model.provider.RemoteDataProvider
import ru.leonov.mytasks.ui.splash.SplashViewModel
import java.io.Console

class App: Application() {
    @ExperimentalCoroutinesApi
    override fun onCreate() {
//        val noteRep = NotesRepository(
//                FireStoreProvider(
//                        FirebaseAuth.getInstance(),
//                        FirebaseFirestore.getInstance()
//                ))
//        val module = SplashViewModel(noteRep)
//        Log.d("ggg", module.toString())
        super.onCreate()
        startKoin(this, listOf(appModule, splashModule, notesModule, currentNoteModule))
    }
}