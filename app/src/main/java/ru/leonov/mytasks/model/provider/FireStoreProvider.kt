package ru.leonov.mytasks.model.provider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import ru.leonov.mytasks.model.data.NoAuthException
import ru.leonov.mytasks.model.data.NoteResult
import ru.leonov.mytasks.model.entities.Note
import ru.leonov.mytasks.model.entities.User
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val store: FirebaseFirestore): RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USER_COLLECTION = "users"
    }

    private val currentUser
        get() = firebaseAuth.currentUser

    private val userNotesCollection: CollectionReference
        get() = currentUser?.let {
            store.collection(USER_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()

    override suspend fun getCurrentUser(): User? = suspendCoroutine {continuation ->
        val user = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
        continuation.resume(user)
    }

    @ExperimentalCoroutinesApi
    override fun subscribeToAllNotes(): ReceiveChannel<NoteResult> =
            Channel<NoteResult>(Channel.CONFLATED).apply {

                var registration: ListenerRegistration? = null

                try {
                    registration = userNotesCollection.addSnapshotListener { snapshot, e ->
                        val value = e?.let {
                            NoteResult.Error(it)
                        } ?: snapshot?.let {
                            val notes = it.documents.map { it.toObject(Note::class.java) }
                            NoteResult.Success(notes)
                        }
                        value?.let {offer(it)}
                    }
                } catch (e: Throwable) { offer(NoteResult.Error(e)) }

                invokeOnClose {
                    registration?.remove()
                }
            }

    override suspend fun getNoteById(id: String): Note = suspendCoroutine {continuation ->
        try {
            userNotesCollection.document(id).get()
                    .addOnSuccessListener { snapshot ->
                        val note = snapshot.toObject(Note::class.java)
                        if (note != null) continuation.resume(note)
                        else continuation.resumeWithException(Exception("Note not found!"))
                    }
                    .addOnFailureListener { Throwable(it) }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun saveNote(note: Note): Note = suspendCoroutine {continuation ->
        try {
            userNotesCollection.document(note.id).set(note)
                    .addOnSuccessListener {
                        continuation.resume(note)
                    }
                    .addOnFailureListener { throw it }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun deleteNote(noteId: String): Unit = suspendCoroutine {continuation ->
        try {
            userNotesCollection.document(noteId).delete()
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }.addOnFailureListener {Throwable(it)}
        } catch (e: Throwable){
            continuation.resumeWithException(e)
        }
    }
}