package ru.leonov.mytasks.model.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatedString(format: String = "dd.mm.yyyy HH:mm:ss",
                  locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}
