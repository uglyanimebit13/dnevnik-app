package com.example.dnevnik.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dnevnik.adapter.DiaryAdapter
import com.example.dnevnik.databinding.ActivityMainBinding
import com.example.dnevnik.repository.DiaryRepository
import com.example.dnevnik.utils.ThemeHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DiaryAdapter
    private var currentSearchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThemeHelper.applyTheme(ThemeHelper.getSavedTheme(this))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = DiaryAdapter(emptyList()) { entry ->
            val intent = Intent(this, DetailEntryActivity::class.java)
            intent.putExtra("entry_id", entry.id)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fabAddEntry.setOnClickListener {
            val intent = Intent(this, EditEntryActivity::class.java)
            startActivity(intent)
        }

        binding.btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentSearchQuery = newText ?: ""
                updateEntriesList()
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        updateEntriesList()
    }

    private fun updateEntriesList() {
        val entries = if (currentSearchQuery.isNotEmpty()) {
            DiaryRepository.searchEntries(currentSearchQuery)
        } else {
            DiaryRepository.getAllEntries()
        }

        adapter.updateEntries(entries)

        if (entries.isEmpty()) {
            binding.emptyStateCard.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyStateCard.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }
}