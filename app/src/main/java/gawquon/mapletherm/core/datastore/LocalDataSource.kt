package gawquon.mapletherm.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Could be used for long-term logging

/*
class LocalDataSource(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userToken")
        private val USER_TOKEN_KEY = floatPreferencesKey("user_token")
    }

    val getAccessToken: Flow<Float> = context.dataStore.data.map { preferences ->
        (preferences[USER_TOKEN_KEY]) ?: 0.0f
    }

    suspend fun saveToken(token: Float) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = token
        }
    }
}*/