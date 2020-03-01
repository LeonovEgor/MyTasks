package ru.leonov.mytasks.ui.note

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.test.rule.ActivityTestRule
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import ru.leonov.mytasks.MainActivity
import ru.leonov.mytasks.model.entities.Note


class CurrentNoteFragmentTest {
    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    private val model: CurrentNoteViewModel = spyk(CurrentNoteViewModel(mockk()))
    private val viewStateLiveData = MutableLiveData<NoteData>()
    private val testNote = Note("1", "title1", "text1")

    @Before
    fun setUp() {
        loadKoinModules(listOf(
                module {
                    viewModel { model }
                }))

        every { model.getViewState() } returns viewStateLiveData
        every { model.loadNote(any()) } just runs
        every { model.save(any()) } just runs
        every { model.delete() } just runs

        val fragmentArgs = Bundle().apply {
            putString("NOTE", "1")
        }

        //TODO: Видимо вместо кода методички будет, что-то подобное
//        activityTestRule
//                .getActivity()
//                .getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.nav_host_fragment, CurrentNoteFragment::class.java, fragmentArgs)
//                .commit()

    }

    @After
    fun tearDown() {
        stopKoin()
    }


}