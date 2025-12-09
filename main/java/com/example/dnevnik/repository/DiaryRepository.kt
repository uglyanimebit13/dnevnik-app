package com.example.dnevnik.repository

import android.util.Log
import com.example.dnevnik.data.DiaryEntry

object DiaryRepository {
    private val entries = mutableListOf<DiaryEntry>()

    init {
        Log.d("DiaryRepo", "Repository initialized. Initial size: ${entries.size}")
    }

    fun getAllEntries(): List<DiaryEntry> {
        Log.d("DiaryRepo", "getAllEntries: returning ${entries.size} entries")
        return entries.toList()
    }

    fun addEntry(entry: DiaryEntry) {
        Log.d("DiaryRepo", "addEntry: adding entry with id=${entry.id}, title=${entry.title}, hasImage=${!entry.imageUri.isNullOrEmpty()}")
        entries.add(0, entry) // добавляем в начало для удобства
        Log.d("DiaryRepo", "addEntry: total entries now ${entries.size}")
    }

    fun updateEntry(updatedEntry: DiaryEntry) {
        Log.d("DiaryRepo", "updateEntry: updating entry with id=${updatedEntry.id}")
        val index = entries.indexOfFirst { it.id == updatedEntry.id }
        if (index != -1) {
            entries[index] = updatedEntry
            Log.d("DiaryRepo", "updateEntry: entry updated at index $index")
        } else {
            Log.e("DiaryRepo", "updateEntry: entry not found! Adding as new.")
            entries.add(0, updatedEntry)
        }
    }

    fun deleteEntry(entryId: Long) {
        Log.d("DiaryRepo", "deleteEntry: deleting entry with id=$entryId")
        val removed = entries.removeAll { it.id == entryId }
        if (removed) {
            Log.d("DiaryRepo", "deleteEntry: entry removed")
        } else {
            Log.d("DiaryRepo", "deleteEntry: no entry found with id=$entryId")
        }
    }

    fun getEntryById(entryId: Long): DiaryEntry? {
        Log.d("DiaryRepo", "getEntryById: looking for entry with id=$entryId")
        val entry = entries.find { it.id == entryId }
        Log.d("DiaryRepo", "getEntryById: found? ${entry != null}")
        return entry
    }

    fun searchEntries(query: String): List<DiaryEntry> {
        Log.d("DiaryRepo", "searchEntries: query=$query")
        return entries.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true)
        }
    }
}