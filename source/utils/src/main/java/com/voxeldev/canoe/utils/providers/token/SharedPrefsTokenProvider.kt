package com.voxeldev.canoe.utils.providers.token

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * @author nvoxel
 */
internal class SharedPrefsTokenProvider(context: Context) : TokenProvider {

    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        SHARED_PREFS_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    private val sharedPreferencesEditor = sharedPreferences.edit()

    override fun getAccessToken(): String? = sharedPreferences.getString(ACCESS_TOKEN_PREF_NAME, null)

    override fun setAccessToken(accessToken: String) {
        sharedPreferencesEditor.putString(ACCESS_TOKEN_PREF_NAME, accessToken)
        sharedPreferencesEditor.apply()
    }

    override fun clearAccessToken() {
        sharedPreferencesEditor.remove(ACCESS_TOKEN_PREF_NAME)
        sharedPreferencesEditor.apply()
    }

    override fun getRefreshToken(): String? = sharedPreferences.getString(REFRESH_TOKEN_PREF_NAME, null)

    override fun setRefreshToken(refreshToken: String) {
        sharedPreferencesEditor.putString(REFRESH_TOKEN_PREF_NAME, refreshToken)
        sharedPreferencesEditor.apply()
    }

    override fun clearRefreshToken() {
        sharedPreferencesEditor.remove(REFRESH_TOKEN_PREF_NAME)
        sharedPreferencesEditor.apply()
    }

    private companion object {
        const val SHARED_PREFS_FILE_NAME = "tokens"
        const val ACCESS_TOKEN_PREF_NAME = "accessToken"
        const val REFRESH_TOKEN_PREF_NAME = "refreshToken"
    }
}
