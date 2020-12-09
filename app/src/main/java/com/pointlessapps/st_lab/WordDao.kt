package com.pointlessapps.st_lab

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(word: Word)

    @Query("DELETE FROM word_table")
    fun deleteAll()

    @Delete
    fun deleteWord(word: Word)

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAllWords(): LiveData<List<Word>>

    @Query("SELECT * FROM word_table LIMIT 1")
    fun getAnyWord(): Array<Word>

    @Update
    fun update(vararg word: Word)
}
