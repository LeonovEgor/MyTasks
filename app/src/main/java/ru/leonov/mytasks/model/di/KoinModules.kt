package ru.leonov.mytasks.model.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.koin.viewModel

import org.koin.dsl.module.module
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.provider.FireStoreProvider
import ru.leonov.mytasks.model.provider.RemoteDataProvider
import ru.leonov.mytasks.ui.note.CurrentNoteViewModel
import ru.leonov.mytasks.ui.notesList.NotesViewModel
import ru.leonov.mytasks.ui.splash.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { NotesRepository(get()) }
}

@ExperimentalCoroutinesApi
val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

@ExperimentalCoroutinesApi
val notesModule = module {
    viewModel { NotesViewModel(get()) }
}

@ExperimentalCoroutinesApi
val currentNoteModule = module {
    viewModel { CurrentNoteViewModel(get()) }
}