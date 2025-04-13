package com.example.symphorb.utils

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// Extensión de acceso a DataStore fuera de la clase singleton
val Context.dataStore by preferencesDataStore(name = "settings")