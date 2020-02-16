package ru.leonov.mytasks.ui.splash

import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import ru.leonov.mytasks.R
import ru.leonov.mytasks.ui.base.BaseFragment

class SplashFragment : BaseFragment<Boolean?, SplashViewState>() {

    override val viewModel by lazy {
        ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override val layoutRes = R.layout.fragment_splash

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, 1000)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            gotoNotesList()
        }
    }

    private fun gotoNotesList() {
        MainActivity.start(this)
        finish()
    }

    override fun showError(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}