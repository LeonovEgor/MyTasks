package ru.leonov.mytasks.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
abstract class BaseFragment<T> : Fragment(), CoroutineScope {

    abstract val viewModel: BaseViewModel<T>
    abstract val layoutRes: Int

    private val job = Job()
    private lateinit var dataJob: Job
    private lateinit var errorJob: Job


    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Main + job
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData();
    }

    private fun initData() {
        dataJob = launch {
            viewModel.getViewChannel().consumeEach {
                renderData(it)
            }
        }

        errorJob = launch {
            viewModel.getErrorChannel().consumeEach {
                renderError(it)
            }
        }
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

    abstract fun renderData(data: T)

    private fun renderError(error: Throwable) = error.message?.let { showError(it) }

    abstract fun showError(error: String)
}