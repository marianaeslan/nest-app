package br.com.gabgrupo.nest.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "nest_prefs")

@Singleton
class TokenDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val ROLE_KEY = stringPreferencesKey("user_role")
    private val USER_ID_KEY = longPreferencesKey("user_id")
    private val NAME_KEY = stringPreferencesKey("user_name")

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs -> prefs[TOKEN_KEY] = token }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { prefs -> prefs[TOKEN_KEY] }.first()
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { prefs -> prefs[ROLE_KEY] = role }
    }

    suspend fun getRole(): String? {
        return context.dataStore.data.map { prefs -> prefs[ROLE_KEY] }.first()
    }

    suspend fun saveUserId(id: Long) {
        context.dataStore.edit { prefs -> prefs[USER_ID_KEY] = id }
    }

    suspend fun getUserId(): Long? {
        return context.dataStore.data.map { prefs -> prefs[USER_ID_KEY] }.first()
    }

    suspend fun saveName(name: String) {
        context.dataStore.edit { prefs -> prefs[NAME_KEY] = name }
    }

    suspend fun getName(): String? {
        return context.dataStore.data.map { prefs -> prefs[NAME_KEY] }.first()
    }

    suspend fun clearAll() {
        context.dataStore.edit { prefs -> prefs.clear() }
    }
}