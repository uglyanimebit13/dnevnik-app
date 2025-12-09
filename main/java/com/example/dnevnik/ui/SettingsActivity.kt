package com.example.dnevnik.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dnevnik.databinding.ActivitySettingsBinding
import com.example.dnevnik.utils.LanguageHelper
import com.example.dnevnik.utils.ThemeHelper

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThemeHelper.applyTheme(ThemeHelper.getSavedTheme(this))

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        loadCurrentSettings()

        binding.buttonSave.setOnClickListener {
            saveSettings()
        }
    }

    private fun loadCurrentSettings() {
        val currentLanguage = LanguageHelper.getSavedLanguage(this)
        binding.chipRussian.isChecked = currentLanguage == "ru"
        binding.chipEnglish.isChecked = currentLanguage == "en"

        val currentTheme = ThemeHelper.getSavedTheme(this)
        binding.switchTheme.isChecked = currentTheme == ThemeHelper.THEME_DARK
    }

    private fun saveSettings() {
        val selectedLanguage = if (binding.chipRussian.isChecked) "ru" else "en"
        val selectedTheme = if (binding.switchTheme.isChecked)
            ThemeHelper.THEME_DARK else ThemeHelper.THEME_LIGHT

        LanguageHelper.saveLanguage(this, selectedLanguage)
        LanguageHelper.applyLanguage(this, selectedLanguage)

        ThemeHelper.saveTheme(this, selectedTheme)
        ThemeHelper.applyTheme(selectedTheme)

        Toast.makeText(
            this,
            if (selectedLanguage == "ru") "Настройки сохранены"
            else "Settings saved",
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}