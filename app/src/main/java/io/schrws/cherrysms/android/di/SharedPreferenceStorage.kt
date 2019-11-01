package io.schrws.cherrysms.android.di

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SharedPreferenceStorage(appContext: Context) {
    companion object {
        const val PREFS_NAME = "pref"
        const val TELEGRAM_ID = "telegram_id"
    }

    private val prefs = appContext.applicationContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

    var telegramID: Long
        set(value) = prefs.edit().putLong(TELEGRAM_ID, value).apply()
        get() = prefs.getLong(TELEGRAM_ID, 0)
}