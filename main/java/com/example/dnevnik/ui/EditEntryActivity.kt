package com.example.dnevnik.ui

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
import com.bumptech.glide.Glide
import com.example.dnevnik.R
import com.example.dnevnik.data.DiaryEntry
import com.example.dnevnik.repository.DiaryRepository
import com.example.dnevnik.utils.ImageHelper
import com.example.dnevnik.utils.ThemeHelper
import java.util.Date

class EditEntryActivity : AppCompatActivity() {
    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText
    private lateinit var imageView: ImageView
    private lateinit var buttonPickImage: Button
    private lateinit var buttonSave: Button

    private var imagePath: String? = null
    private var entryId: Long? = null
    private var originalImagePath: String? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imagePath = ImageHelper.copyImageToAppStorage(this, uri)

            if (imagePath != null) {
                val imageUri = ImageHelper.getImageUriFromPath(this, imagePath)
                if (imageUri != null) {
                    Glide.with(this)
                        .load(imageUri)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_image)
                        .into(imageView)
                    imageView.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(this, "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThemeHelper.applyTheme(ThemeHelper.getSavedTheme(this))
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

                originalImagePath = entry.imageUri
                if (!entry.imageUri.isNullOrEmpty()) {
                    imagePath = entry.imageUri
                    val imageUri = ImageHelper.getImageUriFromPath(this, imagePath)
                    if (imageUri != null) {
                        Glide.with(this)
                            .load(imageUri)
                            .placeholder(R.drawable.ic_image)
                            .error(R.drawable.ic_image)
                            .into(imageView)
                        imageView.visibility = View.VISIBLE
                    }
                }
            }
        }

        buttonPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
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

        try {
            if (entryId != null && imagePath != null && imagePath != originalImagePath) {
                ImageHelper.deleteImage(this, originalImagePath)
            }

            val existingEntry = if (entryId != null) {
                DiaryRepository.getEntryById(entryId!!)
            } else null

            val entry = DiaryEntry(
                id = entryId ?: System.currentTimeMillis(),
                title = title,
                content = content,
                timestamp = existingEntry?.timestamp ?: Date(),
                imageUri = imagePath
            )

            if (entryId != null) {
                DiaryRepository.updateEntry(entry)
                Toast.makeText(this, "Запись обновлена", Toast.LENGTH_SHORT).show()
            } else {
                DiaryRepository.addEntry(entry)
                Toast.makeText(this, "Запись сохранена", Toast.LENGTH_SHORT).show()
            }

            finish()

        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка сохранения: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        if (isFinishing && entryId == null && imagePath != null) {
            ImageHelper.deleteImage(this, imagePath)
        }
        super.onDestroy()
    }
}