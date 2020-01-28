package ru.leonov.mytasks.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_home.view.*
import ru.leonov.mytasks.R

class HomeFragment : Fragment() {

    private var homeViewModel: HomeViewModel? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        homeViewModel!!.text.observe(viewLifecycleOwner, Observer { s -> root.text_home.text = s })

        initButton(root);

        return root
    }

    private fun initButton(view: View ) {
        view.btn_ok.setOnClickListener {
            homeViewModel?.ChangeText("New text");
        }
    }
}