package com.swiftquantum.data.auth

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * UnifiedAuthProvider - ContentProvider for cross-app authentication sharing
 *
 * This provider allows all SwiftQuantum apps (SwiftQuantum, QuantumNative, Q-Bridge, QuantumCareer)
 * to share authentication state. When a user logs in to one app, they're automatically logged
 * into all apps.
 *
 * Authority: com.swiftquantum.auth.provider
 */
class UnifiedAuthProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.swiftquantum.auth.provider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/auth")

        // Column names
        const val COLUMN_ACCESS_TOKEN = "access_token"
        const val COLUMN_REFRESH_TOKEN = "refresh_token"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_USER_EMAIL = "user_email"
        const val COLUMN_USER_NAME = "user_name"
        const val COLUMN_LOGIN_TIMESTAMP = "login_timestamp"
        const val COLUMN_IS_LOGGED_IN = "is_logged_in"

        private const val AUTH_DATA = 1
        private const val PREFS_NAME = "unified_auth_prefs"

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "auth", AUTH_DATA)
        }

        private val ALL_COLUMNS = arrayOf(
            COLUMN_ACCESS_TOKEN,
            COLUMN_REFRESH_TOKEN,
            COLUMN_USER_ID,
            COLUMN_USER_EMAIL,
            COLUMN_USER_NAME,
            COLUMN_LOGIN_TIMESTAMP,
            COLUMN_IS_LOGGED_IN
        )
    }

    private lateinit var encryptedPrefs: android.content.SharedPreferences

    override fun onCreate(): Boolean {
        context?.let { ctx ->
            try {
                val masterKey = MasterKey.Builder(ctx)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()

                encryptedPrefs = EncryptedSharedPreferences.create(
                    ctx,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (e: Exception) {
                // Fallback to regular SharedPreferences if encryption fails
                encryptedPrefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            }
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        when (uriMatcher.match(uri)) {
            AUTH_DATA -> {
                val columns = projection ?: ALL_COLUMNS
                val cursor = MatrixCursor(columns.map { it }.toTypedArray())

                val accessToken = encryptedPrefs.getString(COLUMN_ACCESS_TOKEN, null)
                val refreshToken = encryptedPrefs.getString(COLUMN_REFRESH_TOKEN, null)
                val userId = encryptedPrefs.getString(COLUMN_USER_ID, null)
                val userEmail = encryptedPrefs.getString(COLUMN_USER_EMAIL, null)
                val userName = encryptedPrefs.getString(COLUMN_USER_NAME, null)
                val loginTimestamp = encryptedPrefs.getLong(COLUMN_LOGIN_TIMESTAMP, 0L)
                val isLoggedIn = accessToken != null

                val rowBuilder = cursor.newRow()
                columns.forEach { column ->
                    when (column) {
                        COLUMN_ACCESS_TOKEN -> rowBuilder.add(accessToken)
                        COLUMN_REFRESH_TOKEN -> rowBuilder.add(refreshToken)
                        COLUMN_USER_ID -> rowBuilder.add(userId)
                        COLUMN_USER_EMAIL -> rowBuilder.add(userEmail)
                        COLUMN_USER_NAME -> rowBuilder.add(userName)
                        COLUMN_LOGIN_TIMESTAMP -> rowBuilder.add(loginTimestamp)
                        COLUMN_IS_LOGGED_IN -> rowBuilder.add(if (isLoggedIn) 1 else 0)
                    }
                }

                cursor.setNotificationUri(context?.contentResolver, uri)
                return cursor
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            AUTH_DATA -> {
                values?.let { cv ->
                    encryptedPrefs.edit().apply {
                        cv.getAsString(COLUMN_ACCESS_TOKEN)?.let { putString(COLUMN_ACCESS_TOKEN, it) }
                        cv.getAsString(COLUMN_REFRESH_TOKEN)?.let { putString(COLUMN_REFRESH_TOKEN, it) }
                        cv.getAsString(COLUMN_USER_ID)?.let { putString(COLUMN_USER_ID, it) }
                        cv.getAsString(COLUMN_USER_EMAIL)?.let { putString(COLUMN_USER_EMAIL, it) }
                        cv.getAsString(COLUMN_USER_NAME)?.let { putString(COLUMN_USER_NAME, it) }
                        putLong(COLUMN_LOGIN_TIMESTAMP, System.currentTimeMillis())
                        apply()
                    }
                }
                context?.contentResolver?.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, 1)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        when (uriMatcher.match(uri)) {
            AUTH_DATA -> {
                values?.let { cv ->
                    encryptedPrefs.edit().apply {
                        cv.getAsString(COLUMN_ACCESS_TOKEN)?.let { putString(COLUMN_ACCESS_TOKEN, it) }
                        cv.getAsString(COLUMN_REFRESH_TOKEN)?.let { putString(COLUMN_REFRESH_TOKEN, it) }
                        cv.getAsString(COLUMN_USER_ID)?.let { putString(COLUMN_USER_ID, it) }
                        cv.getAsString(COLUMN_USER_EMAIL)?.let { putString(COLUMN_USER_EMAIL, it) }
                        cv.getAsString(COLUMN_USER_NAME)?.let { putString(COLUMN_USER_NAME, it) }
                        apply()
                    }
                }
                context?.contentResolver?.notifyChange(uri, null)
                return 1
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when (uriMatcher.match(uri)) {
            AUTH_DATA -> {
                encryptedPrefs.edit().clear().apply()
                context?.contentResolver?.notifyChange(uri, null)
                return 1
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            AUTH_DATA -> "vnd.android.cursor.item/vnd.$AUTHORITY.auth"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}
