package ru.leonov.mytasks.ui.notesList

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

import ru.leonov.mytasks.R

class LogoutDialog: DialogFragment() {

    companion object {
        val TAG = LogoutDialog::class.java.name + "TAG"
        fun createInstance() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog =
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.logout_dialog_title))
            .setMessage(getString(R.string.logout_dialog_message))
            .setPositiveButton(getString(R.string.logout_dialog_ok)) { _, _ -> (activity as LogoutListener).onLogout() }
            .setNegativeButton(getString(R.string.logout_dialog_cancel)) { _, _ ->  dismiss() }
            .create()

    interface LogoutListener{
        fun onLogout()
    }
}