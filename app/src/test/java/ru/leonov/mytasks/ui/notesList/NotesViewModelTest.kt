package ru.leonov.mytasks.ui.notesList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.Event
import java.util.*

class NotesViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>()
    private val notesLiveData = MutableLiveData<NoteResult>()

    private lateinit var viewModel: NotesViewModel

    @Before
    fun setup() {
        clearMocks(mockRepository)
        every { mockRepository.getNotes() } returns notesLiveData
        viewModel = NotesViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return notes`() {
        var result: List<Note>? = null
        val testData = listOf(Note("1"), Note("2"))
        viewModel.getViewState().observeForever {
            result = it.data
        }
        notesLiveData.value = NoteResult.Success(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("No data")
        viewModel.getViewState().observeForever {
            result = it.error
        }
        notesLiveData.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()
        assert(notesLiveData.hasActiveObservers())
    }

    @Test
    fun onNoteClick() {
        var result: Note? = null
        val note = Note("1", "Title", "text", Date(), Note.Color.BLUE)
        viewModel.openNoteEvent.observeForever {event ->
            event.getContentIfNotHandled()?.let { note ->
                result = note
            }
        }
        viewModel.onNoteClick(note)

        assertEquals(note, result)
    }

    @Test
    fun onNewNoteClick() {
        var result: Boolean = false
        val note = Note("2", "", "", Date(), Note.Color.WHITE)
        viewModel.openNewNoteEvent.observeForever {event ->
            event.getContentIfNotHandled()?.let {
                result = true
            }
        }
        viewModel.onNewNoteClick()

        assert(result)
    }
}