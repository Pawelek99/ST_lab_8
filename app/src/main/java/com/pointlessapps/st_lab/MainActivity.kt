package com.pointlessapps.st_lab

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val wordViewModel: WordViewModel
        get() = ViewModelProviders.of(this).get(WordViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = wordViewModel

        recyclerview.apply {
            adapter = WordListAdapter()
            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        }

        viewModel.getAllWords().observe(this) {
            (recyclerview.adapter as? WordListAdapter)?.setWords(it)
        }

        fab.setOnClickListener {
            startActivityForResult(Intent(this, NewWordActivity::class.java), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            wordViewModel.insert(Word(data?.getStringExtra("reply") ?: ""))
        } else {
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_SHORT).show()
        }
    }
}
