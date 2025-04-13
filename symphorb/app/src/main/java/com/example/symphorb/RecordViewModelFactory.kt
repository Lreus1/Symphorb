package com.example.symphorb.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecordViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica que modelClass sea RecordViewModel antes de hacer el cast
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            // Directamente crea el ViewModel sin necesidad de usar 'as'
            @Suppress("UNCHECKED_CAST")
            return RecordViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}