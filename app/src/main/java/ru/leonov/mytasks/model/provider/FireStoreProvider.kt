package ru.leonov.mytasks.model.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.entities.Note

class FireStoreProvider: RemoteDataProvider {
    private val TAG = "${FireStoreProvider::class.java.simpleName} :"

    private val db : FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val notesReference = db.collection(NOTES_COLLECTION)

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    override fun subsrcibeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.addSnapshotListener{snapshot, e ->
            e?.let {
                Log.e(TAG, "Notes load Alarm!!! $it")
                result.value = NoteResult.Error(it)
            }
            snapshot?.let {
                Log.d(TAG, "Load Notes success")
                val notes = mutableListOf<Note>()

                for (doc: QueryDocumentSnapshot in it) {
                    val note = doc.toObject(Note::class.java)
                    Log.d(TAG, "$note")
                    notes.add(note)
                }

                result.value = NoteResult.Success(notes)
            }
        }

        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {

        val result = MutableLiveData<NoteResult>()

        notesReference.document(id).get()
                .addOnSuccessListener {snapshot ->
                    val note = snapshot.toObject(Note::class.java)
                    Log.d(TAG, "Get Note success: $id - $note")
                    result.value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    Log.e(TAG, "Get Note Alarm!!! $it")
                    result.value = NoteResult.Error(it)
                }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(note.id).set(note)
                .addOnSuccessListener {
                    Log.d(TAG, "Note $note is saved")
                    result.value = NoteResult.Success(note)
        }
                .addOnFailureListener {
                    Log.e(TAG, "Alarm!!! Note $note is not saved!!!: $it")
                    result.value = NoteResult.Error(it)
        }
        return result
    }
}