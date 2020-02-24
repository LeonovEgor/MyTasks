package ru.leonov.mytasks.ui.notesList

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import org.koin.standalone.StandAloneContext.stopKoin
import ru.leonov.mytasks.MainActivity
import ru.leonov.mytasks.R
import ru.leonov.mytasks.model.entities.Note

class NotesFragmentTest {
    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val model: NotesViewModel = mockk(relaxed = true)
    private val viewStateLiveData = MutableLiveData<NotesViewState>()

    private val testNotes = listOf(
            Note("1", "title1", "text1"),
            Note("2", "title2", "text2"),
            Note("3", "title3", "text3")
    )


    @Before
    fun setUp() {
        StandAloneContext.loadKoinModules(
                listOf( module { viewModel(override = true) { model } } )
        )

        every { model.getViewState() } returns viewStateLiveData
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(NotesViewState(notes = testNotes))
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_data_is_displayed(){
        Espresso
                .onView(withId(R.id.rv_notes))
                .perform(RecyclerViewActions.scrollToPosition<NotesRVAdapter.ViewHolder>(1))
        Espresso
                .onView(ViewMatchers.withText(testNotes[1].text))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //Failed test
    @Test
    fun check_detail_activity_intent_sent() {

        Espresso
                .onView(withId(R.id.rv_notes))
                .perform(actionOnItemAtPosition<NotesRVAdapter.ViewHolder>(1, click()))

        //TODO: У меня сделан переход через viewModel и Navigation. Не знаю, как это тут обыграть.
        intended(allOf(hasComponent(NotesFragment::class.java.name),
                hasExtra("NOTE", testNotes[0].id)))
    }
}