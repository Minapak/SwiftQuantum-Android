package com.swiftquantum.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.swiftquantum.domain.model.UserTier
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userPrefsDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val USER_TIER_KEY = stringPreferencesKey("user_tier")
        private val DEFAULT_QUBITS_KEY = intPreferencesKey("default_qubits")
        private val DEFAULT_SHOTS_KEY = intPreferencesKey("default_shots")
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        private val SHOW_STATE_VECTOR_KEY = booleanPreferencesKey("show_state_vector")
        private val AUTO_SAVE_CIRCUITS_KEY = booleanPreferencesKey("auto_save_circuits")
        private val PREFERRED_BACKEND_KEY = stringPreferencesKey("preferred_backend")
        private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
        private val LANGUAGE_KEY = stringPreferencesKey("language")
    }

    val userTier: Flow<UserTier> = context.userPrefsDataStore.data.map { preferences ->
        try {
            UserTier.valueOf(preferences[USER_TIER_KEY] ?: UserTier.FREE.name)
        } catch (e: Exception) {
            UserTier.FREE
        }
    }

    val defaultQubits: Flow<Int> = context.userPrefsDataStore.data.map { preferences ->
        preferences[DEFAULT_QUBITS_KEY] ?: 2
    }

    val defaultShots: Flow<Int> = context.userPrefsDataStore.data.map { preferences ->
        preferences[DEFAULT_SHOTS_KEY] ?: 1024
    }

    val isDarkMode: Flow<Boolean> = context.userPrefsDataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: true
    }

    val showStateVector: Flow<Boolean> = context.userPrefsDataStore.data.map { preferences ->
        preferences[SHOW_STATE_VECTOR_KEY] ?: false
    }

    val autoSaveCircuits: Flow<Boolean> = context.userPrefsDataStore.data.map { preferences ->
        preferences[AUTO_SAVE_CIRCUITS_KEY] ?: true
    }

    val preferredBackend: Flow<String> = context.userPrefsDataStore.data.map { preferences ->
        preferences[PREFERRED_BACKEND_KEY] ?: "RUST_SIMULATOR"
    }

    val onboardingCompleted: Flow<Boolean> = context.userPrefsDataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED_KEY] ?: false
    }

    val language: Flow<String?> = context.userPrefsDataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY]
    }

    suspend fun setUserTier(tier: UserTier) {
        context.userPrefsDataStore.edit { preferences ->
            preferences[USER_TIER_KEY] = tier.name
        }
    }

    suspend fun setDefaultQubits(qubits: Int) {
        context.userPrefsDataStore.edit { preferences ->
            preferences[DEFAULT_QUBITS_KEY] = qubits
        }
    }

    suspend fun setDefaultShots(shots: Int) {
        context.userPrefsDataStore.edit { preferences ->
            preferences[DEFAULT_SHOTS_KEY] = shots
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.userPrefsDataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    suspend fun setShowStateVector(show: Boolean) {
        context.userPrefsDataStore.edit { preferences ->
            preferences[SHOW_STATE_VECTOR_KEY] = show
        }
    }

    suspend fun setAutoSaveCircuits(enabled: Boolean) {
        context.userPrefsDataStore.edit { preferences ->
            preferences[AUTO_SAVE_CIRCUITS_KEY] = enabled
        }
    }

    suspend fun setPreferredBackend(backend: String) {
        context.userPrefsDataStore.edit { preferences ->
            preferences[PREFERRED_BACKEND_KEY] = backend
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.userPrefsDataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] = completed
        }
    }

    suspend fun setLanguage(languageCode: String) {
        context.userPrefsDataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }

    suspend fun getLanguage(): String? {
        return language.first()
    }

    suspend fun getUserTier(): UserTier {
        return userTier.first()
    }

    suspend fun clearAll() {
        context.userPrefsDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
