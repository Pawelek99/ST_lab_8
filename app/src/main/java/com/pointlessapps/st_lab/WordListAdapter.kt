package com.pointlessapps.st_lab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordListAdapter : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    private val mWords = mutableListOf<Word>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false))
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.wordItemView.text = mWords[position].word
    }

    fun setWords(words: List<Word>) {
        mWords.clear()
        mWords.addAll(words)
        notifyDataSetChanged()
    }

    override fun getItemCount() = mWords.size

    class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordItemView: TextView = view.findViewById(R.id.textView)
    }
}
