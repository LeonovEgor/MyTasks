package ru.leonov.mytasks.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.android.viewmodel.ext.android.viewModel
import ru.leonov.mytasks.MainActivity
import ru.leonov.mytasks.R
import kotlin.coroutines.CoroutineContext

class SplashActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        private const val RC_SIGN_IN = 458
    }

    private val job = Job()
    private lateinit var dataJob: Job
    private lateinit var errorJob: Job

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Main + job
    }

    @ExperimentalCoroutinesApi
    private val viewModel: SplashViewModel by viewModel()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    @ExperimentalCoroutinesApi
    private fun initData() {
        dataJob = launch {
            viewModel.getViewChannel().consumeEach {
                gotoNotesList()
            }
        }

        errorJob = launch {
            viewModel.getErrorChannel().consumeEach {
                startLogin()
            }
        }
    }

    @ExperimentalCoroutinesApi
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

        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
    }

    private fun gotoNotesList() {
        MainActivity.start(this)
        finish()
    }

    override fun onStop() {
        super.onStop()
        dataJob.cancel()
        errorJob.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }

}