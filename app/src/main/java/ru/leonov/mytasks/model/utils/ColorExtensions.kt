package ru.leonov.mytasks.model.utils

import android.content.Context
import androidx.core.content.ContextCompat
import ru.leonov.mytasks.R
import ru.leonov.mytasks.model.entities.Note

fun Note.Color.getColorInt(context: Context): Int =
    ContextCompat.getColor(
            context, when (this) {
        Note.Color.WHITE -> R.color.white
        Note.Color.VIOLET -> R.color.violet
        Note.Color.YELLOW -> R.color.yellow
        Note.Color.RED -> R.color.red
        Note.Color.PINK -> R.color.pink
        Note.Color.GREEN -> R.color.green
        Note.Color.BLUE -> R.color.blue
    })

fun Note.Color.getColorRes(): Int = when (this) {
    Note.Color.WHITE -> R.color.white
    Note.Color.VIOLET -> R.color.violet
    Note.Color.YELLOW -> R.color.yellow
    Note.Color.RED -> R.color.red
    Note.Color.PINK -> R.color.pink
    Note.Color.GREEN -> R.color.green
    Note.Color.BLUE -> R.color.blue
}