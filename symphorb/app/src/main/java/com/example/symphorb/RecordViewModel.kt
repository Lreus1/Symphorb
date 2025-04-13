package com.example.symphorb.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.symphorb.utils.DataStoreManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class RecordViewModel(private val context: Context) : ViewModel() {

    val record: Flow<Int> = DataStoreManager.obtenerRecord(context)

    fun guardarRecord(nuevoRecord: Int) {
        viewModelScope.launch {
            DataStoreManager.guardarRecord(context, nuevoRecord)
        }
    }

    fun resetearRecord() {
        viewModelScope.launch {
            DataStoreManager.guardarRecord(context, 0)
        }
    }
}