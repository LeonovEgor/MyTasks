package ru.leonov.mytasks.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val mText: MutableLiveData<String>
    private var counter: Int

    val text: LiveData<String>
        get() = mText

    init {
        mText = MutableLiveData()
        mText.value = "This is home fragment"
        counter = 0
    }

    fun ChangeText(newText: String) {
        mText.value = newText + " " + counter++
    }

}