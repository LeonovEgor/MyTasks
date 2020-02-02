package ru.leonov.mytasks.model.data

import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.getCurrentDateTime

object NotesRepository {
    val notes: List<Note> = listOf(
        Note(
            "Заметка 1",
            "Текст заметки 1. Не очень длинный, но интересный",
                getCurrentDateTime(),
            0xfff06292.toInt()
        ),
        Note(
            "Заметка 2",
            "Текст заметки 2. Не очень длинный, но интересный",
                getCurrentDateTime(),
            0xff9575cd.toInt()
        ),
        Note(
            "Заметка 3",
            "Текст заметки 3. Не очень длинный, но интересный",
                getCurrentDateTime(),
            0xff64b5f6.toInt()
        ),
        Note(
            "Заметка 4",
            "Текст заметки 4. Не очень длинный, но интересный",
                getCurrentDateTime(),
            0xff4db6ac.toInt()
        ),
        Note(
            "Заметка 5",
            "Текст заметки 5. Не очень длинный, но интересный",
                getCurrentDateTime(),
            0xffb2ff59.toInt()
        ),
        Note(
            "Заметка 6",
            "Текст заметки 6. Не очень длинный, но интересный",
                getCurrentDateTime(),
            0xffffeb3b.toInt()
        )
    )
}