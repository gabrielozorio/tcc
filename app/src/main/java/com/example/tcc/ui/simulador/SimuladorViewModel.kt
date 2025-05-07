package com.example.tcc.ui.simulador

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimuladorViewModel : ViewModel() {



    private val _text = MutableLiveData<String>().apply {
        value = "This is simulador Fragment"
    }
    val text: LiveData<String> = _text
}