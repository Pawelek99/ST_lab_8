package com.pointlessapps.st_lab

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WordRepository(context: Context) {
    private val wordDao = WordRoomDatabase.init(context).wordDao()
    private val mAllWords = wordDao.getAllWords()

    fun getAllWords() = mAllWords

    fun insert(word: Word) {
        GlobalScope.launch(Dispatchers.IO) {
            wordDao.insert(word)
        }
    }

    fun deleteAll() {
        GlobalScope.launch(Dispatchers.IO) {
            wordDao.deleteAll()
        }
    }

    fun deleteWord(word: Word) {
        GlobalScope.launch(Dispatchers.IO) {
            wordDao.deleteWord(word)
        }
    }
}
