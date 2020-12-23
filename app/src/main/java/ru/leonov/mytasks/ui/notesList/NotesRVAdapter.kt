package ru.leonov.mytasks.ui.notesList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.leonov.mytasks.R
import ru.leonov.mytasks.databinding.ItemNoteBinding
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.utils.formattedString
import ru.leonov.mytasks.model.utils.getColorInt

class NotesRVAdapter(val onItemViewClick : ((note: Note) -> Unit)? = null)
    : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(vh: ViewHolder, pos: Int) = vh.bind(notes[pos])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemNoteBinding.bind(itemView)

        @SuppressLint("DefaultLocale")
        fun bind(note: Note) = with(itemView) {
            binding.tvTitle.text = note.title.toUpperCase()
            binding.tvText.text = note.text
            binding.tvDate.text = note.date.formattedString()

            (this as CardView).setCardBackgroundColor(note.color.getColorInt(context))
            itemView.setOnClickListener {
                onItemViewClick?.invoke(note)
            }
        }
    }

}