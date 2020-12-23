package ru.leonov.mytasks.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Note (
         val id: String = "",
         val title: String = "",
         val text: String = "",
         val date: Date = Date(),
         val color: Color = Color.WHITE) : Parcelable {

    enum class Color{
        WHITE,
        YELLOW,
        GREEN,
        BLUE,
        RED,
        VIOLET,
        PINK
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        return id == other.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + color.hashCode()
        return result
    }

}