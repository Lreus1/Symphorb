package com.example.symphorb.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreManager {

    private val RECORD_KEY = intPreferencesKey("record_maximo")

    suspend fun guardarRecord(context: Context, record: Int) {
        context.dataStore.edit { preferences ->
            preferences[RECORD_KEY] = record
        }
    }

    fun obtenerRecord(context: Context): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[RECORD_KEY] ?: 0
        }
    }
}