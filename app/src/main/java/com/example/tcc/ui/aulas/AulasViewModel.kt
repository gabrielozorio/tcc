package com.example.tcc.ui.aulas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AulasViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is aulas Fragment"
    }
    val text: LiveData<String> = _text
}