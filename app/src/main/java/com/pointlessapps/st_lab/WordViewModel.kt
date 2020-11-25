package com.pointlessapps.st_lab

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class WordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WordRepository(application)
    private val mAllWords = repository.getAllWords()

    fun getAllWords() = mAllWords

    fun insert(word: Word) = repository.insert(word)
}
