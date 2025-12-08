package com.example.dnevnik.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.dnevnik.R
import com.example.dnevnik.data.DiaryEntry
import com.example.dnevnik.repository.DiaryRepository

class EditEntryActivity : AppCompatActivity() {
    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText
    private lateinit var imageView: ImageView
    private lateinit var buttonPickImage: Button
    private lateinit var buttonSave: Button

    private var imageUri: Uri? = null
    private var entryId: Long? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            imageView.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_entry)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        editTitle = findViewById(R.id.editTitle)
        editContent = findViewById(R.id.editContent)
        imageView = findViewById(R.id.imageView)
        buttonPickImage = findViewById(R.id.buttonPickImage)
        buttonSave = findViewById(R.id.buttonSave)

        entryId = intent.getLongExtra("entry_id", -1).takeIf { it != -1L }

        if (entryId != null) {
            val entry = DiaryRepository.getEntryById(entryId!!)
            if (entry != null) {
                editTitle.setText(entry.title)
                editContent.setText(entry.content)
                if (entry.imageUri != null) {
                    imageUri = entry.imageUri?.toUri()
                    imageView.setImageURI(imageUri)
                    imageView.visibility = View.VISIBLE
                }
            }
        }

        buttonPickImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }

        buttonSave.setOnClickListener {
            saveEntry()
        }
    }

    private fun saveEntry() {
        val title = editTitle.text.toString().trim()
        val content = editContent.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Введите заголовок", Toast.LENGTH_SHORT).show()
            return
        }

        val entry = DiaryEntry(
            id = entryId ?: System.currentTimeMillis(),
            title = title,
            content = content,
            imageUri = imageUri?.toString()
        )

        if (entryId != null) {
            DiaryRepository.updateEntry(entry)
            Toast.makeText(this, "Запись обновлена", Toast.LENGTH_SHORT).show()
        } else {
            DiaryRepository.addEntry(entry)
            Toast.makeText(this, "Запись сохранена", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}