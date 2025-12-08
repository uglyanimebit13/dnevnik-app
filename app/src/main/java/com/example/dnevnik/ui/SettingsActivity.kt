package com.example.dnevnik.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dnevnik.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.chipRussian.setOnClickListener {
            binding.chipRussian.isChecked = true
            binding.chipEnglish.isChecked = false
        }

        binding.chipEnglish.setOnClickListener {
            binding.chipEnglish.isChecked = true
            binding.chipRussian.isChecked = false
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
        }
    }
}