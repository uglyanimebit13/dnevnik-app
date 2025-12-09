package com.example.dnevnik.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dnevnik.R
import com.example.dnevnik.repository.DiaryRepository
import com.example.dnevnik.utils.ImageHelper
import com.example.dnevnik.utils.ThemeHelper
import java.text.SimpleDateFormat
import java.util.Locale

class DetailEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThemeHelper.applyTheme(ThemeHelper.getSavedTheme(this))
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
            Toast.makeText(this, "Ошибка: запись не найдена", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val entry = DiaryRepository.getEntryById(entryId)
        if (entry == null) {
            Toast.makeText(this, "Запись не найдена", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        textTitle.text = entry.title
        textContent.text = entry.content

        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        textDate.text = dateFormat.format(entry.timestamp)

        if (!entry.imageUri.isNullOrEmpty()) {
            val imageUri = ImageHelper.getImageUriFromPath(this, entry.imageUri)
            if (imageUri != null) {
                Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .into(imageView)
                imageView.visibility = View.VISIBLE
            } else {
                imageView.visibility = View.GONE
            }
        } else {
            imageView.visibility = View.GONE
        }

        buttonEdit.setOnClickListener {
            val intent = Intent(this, EditEntryActivity::class.java)
            intent.putExtra("entry_id", entry.id)
            startActivity(intent)
        }

        buttonDelete.setOnClickListener {
            if (!entry.imageUri.isNullOrEmpty()) {
                ImageHelper.deleteImage(this, entry.imageUri)
            }
            DiaryRepository.deleteEntry(entry.id)
            Toast.makeText(this, "Запись удалена", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}