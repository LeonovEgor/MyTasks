package ru.leonov.mytasks.model.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.formattedString(format: String = "dd.mm.yyyy HH:mm:ss",
                         locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun Date.getDateString(): String = this.formattedString()
