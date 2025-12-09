package com.example.dnevnik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dnevnik.R
import com.example.dnevnik.data.DiaryEntry
import java.text.SimpleDateFormat
import java.util.Locale

class DiaryAdapter(
    private var entries: List<DiaryEntry>,
    private val onItemClick: (DiaryEntry) -> Unit
) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.entryTitle)
        val contentTextView: TextView = itemView.findViewById(R.id.entryContent)
        val dateTextView: TextView = itemView.findViewById(R.id.entryDate)
        val hasImageIcon: ImageView = itemView.findViewById(R.id.entryHasImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diary_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.titleTextView.text = entry.title

        val contentPreview = if (entry.content.length > 100) {
            entry.content.substring(0, 100) + "..."
        } else {
            entry.content
        }
        holder.contentTextView.text = contentPreview
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        holder.dateTextView.text = dateFormat.format(entry.timestamp)

        holder.hasImageIcon.visibility = if (!entry.imageUri.isNullOrEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClick(entry)
        }
    }

    override fun getItemCount(): Int = entries.size

    fun updateEntries(newEntries: List<DiaryEntry>) {
        val diffCallback = DiaryDiffCallback(entries, newEntries)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        entries = newEntries
        diffResult.dispatchUpdatesTo(this)
    }
}

class DiaryDiffCallback(
    private val oldList: List<DiaryEntry>,
    private val newList: List<DiaryEntry>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.title == newItem.title &&
                oldItem.content == newItem.content &&
                oldItem.imageUri == newItem.imageUri &&
                oldItem.timestamp == newItem.timestamp
    }
}