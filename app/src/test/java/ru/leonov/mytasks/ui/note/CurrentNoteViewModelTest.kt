package ru.leonov.mytasks.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.data.NotesRepository
import ru.leonov.mytasks.model.entities.Note

class CurrentNoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>()
    private val noteLiveData = MutableLiveData<NoteResult>()

    private val testNote = Note("1", "title", "text")

    private lateinit var viewModel: CurrentNoteViewModel


    @Before
    fun setUp() {
        clearMocks(mockRepository)
        every { mockRepository.getNoteById(testNote.id) } returns noteLiveData
        every { mockRepository.deleteNote(testNote.id) } returns noteLiveData
        every { mockRepository.saveNote(testNote) } returns  noteLiveData
        viewModel = CurrentNoteViewModel(mockRepository)
    }

    @Test
    fun `loadNote should return CurrentNoteViewState data`() {
        var result: NoteData.Data? = null
        val testData = NoteData.Data(false, testNote)
        viewModel.getViewState().observeForever {
            result = it.data
        }

        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)
        assertEquals(testData, result)
    }

    @Test
    fun `loadNote should return error`() {
        var result: Throwable? = null
        val testData = Exception("No data")
        viewModel.getViewState().observeForever {
            result = it.error
        }

        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }

    @Test
    fun `deleteNote should return NoteViewState Data wish isDeleted`() {
        var result: NoteData.Data? = null
        val testData = NoteData.Data(true, null)
        viewModel.getViewState().observeForever {
            result = it.data
        }

        viewModel.save(testNote)
        viewModel.delete()
        noteLiveData.value = NoteResult.Success(null)
        assertEquals(testData, result)
    }

    @Test
    fun `deleteNote should return error`() {
        var result: Throwable? = null
        val testData = Throwable("Error")
        viewModel.getViewState().observeForever {
            result = it.error
        }

        viewModel.save(testNote)
        viewModel.delete()
        noteLiveData.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should save changes`() {
        viewModel.save(testNote)
        viewModel.onCleared()
        verify { mockRepository.saveNote(testNote) }
    }

    @Test
    fun `exit should save changes`() {
        viewModel.save(testNote)
        viewModel.gotoNotesList()
        verify { mockRepository.saveNote(testNote) }
    }

    @Test
    fun getGotoNotesListEvent() {
        var result: Boolean = false

        viewModel.gotoNotesListEvent.observeForever {event ->
            event.getContentIfNotHandled()?.let {
                result = true
            }
        }

        viewModel.gotoNotesList()
        assert(result)
    }
}