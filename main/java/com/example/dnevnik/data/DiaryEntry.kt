package com.example.dnevnik.data

import java.util.Date

data class DiaryEntry(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val content: String,
    val timestamp: Date = Date(),
    val imageUri: String? = null
)