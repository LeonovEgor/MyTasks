package ru.leonov.mytasks.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

abstract class BaseFragment<T, S : BaseViewState<T>> : Fragment() {

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

    private fun renderError(error: Throwable) = error.message?.let { showError(it) }

    abstract fun showError(error: String)
}