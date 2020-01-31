package ru.leonov.mytasks.model.repository

import ru.leonov.mytasks.model.entities.Note

interface INoteRepository {
    fun getNotes(): List<Note>
}