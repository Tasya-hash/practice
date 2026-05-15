package com.example.auth.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.managers.AuthManager
import com.example.domain.models.AuthState
import com.example.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.google.gson.Gson

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class AuthManagerImpl(private val context: Context) : AuthManager {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    override fun observeAuthState(): Flow<AuthState> = _authState

    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            val user = getCurrentUser()
            _authState.value = if (user != null) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated
            }
        }
    }

    override fun getCurrentUser(): User? {
        // В реальном приложении читаем из DataStore
        return null
    }

    override fun isLoggedIn(): Boolean {
        return getCurrentUser() != null
    }

    override suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
        _authState.value = AuthState.Unauthenticated
    }

    override suspend fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("current_user")] = userJson
        }
        _authState.value = AuthState.Authenticated
    }

    override fun getUserId(): Long? {
        return getCurrentUser()?.id
    }
}