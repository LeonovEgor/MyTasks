package ru.leonov.mytasks.model.provider

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.leonov.mytasks.model.data.NoAuthException
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.entities.User

class FireStoreProvider: RemoteDataProvider {
    private val TAG = "${FireStoreProvider::class.java.simpleName} :"

    private val db : FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USER_COLLECTION = "users"
    }

    private val userNotesCollection = currentUser?.let {
        db.collection(USER_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun getCurrentUser() = MutableLiveData<User?>().apply {
        value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
    }
    override fun subscribeToAllNotes() = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.addSnapshotListener { snapshot, e ->
                e?.let {
                    throw it
                } ?: let {
                    Log.d(TAG, "Load Notes success")
                    snapshot?.let { snapshot ->
                        value = NoteResult.Success(snapshot.map { it.toObject(Note::class.java) })
                    }
                }
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Notes load Alarm!!! $e")
            NoteResult.Error(e)
        }
    }

    override fun getNoteById(id: String) = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(id).get()
                    .addOnSuccessListener { snapshot ->
                        val note = snapshot.toObject(Note::class.java)
                        Log.d(TAG, "Get Note success: $id - $note")
                        value = NoteResult.Success(note)
                    }
                    .addOnFailureListener { throw it }
        } catch (e: Throwable) {
            Log.e(TAG, "Alarm!!! getNoteById $id:  $e")
            value = NoteResult.Error(e)
        }
    }

    override fun saveNote(note: Note) = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(note.id).set(note)
                    .addOnSuccessListener {
                        Log.d(TAG, "Note $note is saved")
                        value = NoteResult.Success(note)
                    }
                    .addOnFailureListener { throw it }
        } catch (e: Throwable) {
            Log.e(TAG, "Alarm!!! Note $note is not saved!!!: $e")
            value = NoteResult.Error(e)
        }
    }
}