package com.pointlessapps.st_lab

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
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

            ItemTouchHelper(
                object :
                    ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ) = false

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val word =
                            (adapter as WordListAdapter).getWordAtPosition(viewHolder.adapterPosition)
                        Toast.makeText(this@MainActivity, "Deleting $word", Toast.LENGTH_LONG)
                            .show()

                        wordViewModel.deleteWord(word)
                    }
                }
            ).attachToRecyclerView(this)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_data) {
            Toast.makeText(this, "Clearing the data...", Toast.LENGTH_SHORT).show()

            wordViewModel.deleteAll()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
