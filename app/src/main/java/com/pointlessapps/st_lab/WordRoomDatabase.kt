package com.pointlessapps.st_lab

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object : Utils.SingletonHolder<WordRoomDatabase, Context>({
        Room.databaseBuilder(
            it!!,
            WordRoomDatabase::class.java,
            "word_database"
        ).fallbackToDestructiveMigration().addCallback(WordRoomDatabase.callback).build()
    }) {
        private val callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                GlobalScope.launch(Dispatchers.IO) {
                    instance?.wordDao()?.deleteAll()
                    listOf("dolphin", "crocodile", "cobra").forEach {
                        instance?.wordDao()?.insert(Word(it))
                    }
                }
            }
        }
    }
}
