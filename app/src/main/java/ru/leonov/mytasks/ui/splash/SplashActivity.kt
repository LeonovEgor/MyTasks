package ru.leonov.mytasks.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import org.koin.android.viewmodel.ext.android.viewModel
import ru.leonov.mytasks.MainActivity
import ru.leonov.mytasks.R

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 458
    }

    private val viewModel: SplashViewModel by viewModel()
//            by lazy {
//        ViewModelProvider(this).get(SplashViewModel::class.java)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getViewState().observe(this, Observer<SplashViewState> { t ->
            t?.apply {
                data?.let { gotoNotesList() }
                error?.let { startLogin() }
            }
        })
    }


    override fun onResume() {
        super.onResume()

        Handler().postDelayed({ viewModel.requestUser() }, 1000)
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

        if(requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK){
            finish()
        }
    }

    private fun gotoNotesList() {
        MainActivity.start(this)
        finish()
    }
}