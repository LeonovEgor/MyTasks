package ru.leonov.mytasks.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import ru.leonov.mytasks.R
import ru.leonov.mytasks.model.data.NoAuthException

abstract class BaseFragment<T, S : BaseViewState<T>> : Fragment() {
    companion object {
        private const val RC_SIGN_IN = 458
    }

    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.getViewState().observe(viewLifecycleOwner, Observer<S> { t ->
                t?.apply {
                    data?.let { renderData(it) }
                    error?.let { renderError(it) }
            }
        })

        return inflater.inflate(layoutRes, container, false)
    }

    abstract fun renderData(data: T)

    private fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLogin()
            else -> error.message?.let { showError(it) }
        }
    }
    private fun startLogin() {
        val providers = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.android_robot)
                        .setTheme(R.style.LoginStyle)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
    }


    abstract fun showError(error: String)
}