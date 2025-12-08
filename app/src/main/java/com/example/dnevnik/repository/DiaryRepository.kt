package com.example.dnevnik.repository

import com.example.dnevnik.data.DiaryEntry

object DiaryRepository {
    private val entries = mutableListOf<DiaryEntry>()

    fun getAllEntries(): List<DiaryEntry> {
        return entries.toList()
    }

    fun addEntry(entry: DiaryEntry) {
        entries.add(entry)
    }

    fun updateEntry(updatedEntry: DiaryEntry) {
        val index = entries.indexOfFirst { it.id == updatedEntry.id }
        if (index != -1) {
            entries[index] = updatedEntry
        }
    }

    fun deleteEntry(entryId: Long) {
        entries.removeAll { it.id == entryId }
    }

    fun getEntryById(entryId: Long): DiaryEntry? {
        return entries.find { it.id == entryId }
    }

    fun searchEntries(query: String): List<DiaryEntry> {
        return entries.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true)
        }
    }
}