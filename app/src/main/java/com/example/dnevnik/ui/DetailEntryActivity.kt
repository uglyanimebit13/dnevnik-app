package com.example.dnevnik.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.dnevnik.R
import com.example.dnevnik.repository.DiaryRepository

class DetailEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_entry)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

            val textTitle = findViewById<TextView>(R.id.textTitle)
        val textContent = findViewById<TextView>(R.id.textContent)
        val textDate = findViewById<TextView>(R.id.textDate)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val buttonEdit = findViewById<Button>(R.id.buttonEdit)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)

        val entryId = intent.getLongExtra("entry_id", -1)
        if (entryId == -1L) {
            finish()
            return
        }

        val entry = DiaryRepository.getEntryById(entryId)
        if (entry == null) {
            finish()
            return
        }

        textTitle.text = entry.title
        textContent.text = entry.content
        textDate.text = entry.timestamp.toString()

        if (entry.imageUri != null) {
            Glide.with(this)
                .load(entry.imageUri)
                .into(imageView)
            imageView.visibility = android.view.View.VISIBLE
        }

        buttonEdit.setOnClickListener {
            val intent = Intent(this, EditEntryActivity::class.java)
            intent.putExtra("entry_id", entry.id)
            startActivity(intent)
        }

        buttonDelete.setOnClickListener {
            DiaryRepository.deleteEntry(entry.id)
            Toast.makeText(this, "Запись удалена", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}