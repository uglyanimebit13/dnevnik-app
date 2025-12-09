package com.example.dnevnik.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LanguageHelper {

    private const val PREFS_NAME = "dnevnik_settings"
    private const val KEY_LANGUAGE = "language"
    private const val DEFAULT_LANGUAGE = "en"

    fun saveLanguage(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
    }

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun applyLanguage(context: Context, languageCode: String) {
        val locale = when (languageCode) {
            "ru" -> Locale("ru", "RU")
            else -> Locale.ENGLISH
        }

        Locale.setDefault(locale)

        val resources = context.resources
        val config = Configuration(resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }

        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}