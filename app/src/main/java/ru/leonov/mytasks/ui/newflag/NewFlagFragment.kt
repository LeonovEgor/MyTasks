package ru.leonov.mytasks.ui.newflag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.leonov.mytasks.R

class NewFlagFragment : Fragment() {

    private var newFlagViewModel: NewFlagViewModel? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        newFlagViewModel = ViewModelProvider(this).get(NewFlagViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_flag, container, false)
        val textView = root.findViewById<TextView>(R.id.text_send)
        newFlagViewModel!!.text.observe(viewLifecycleOwner, Observer { s -> textView.text = s })
        return root
    }
}