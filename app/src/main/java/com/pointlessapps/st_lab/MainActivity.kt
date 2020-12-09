package com.pointlessapps.st_lab

import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    companion object {
        private val BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?"
        private val QUERY_PARAM = "q"
        private val MAX_RESULTS = "maxResults"
        private val PRINT_TYPE = "printType"
    }

    private val results = mutableListOf<Pair<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
                window.decorView.rootView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )

            loader.isVisible = true
            GlobalScope.launch(Dispatchers.IO) {
                (URL(
                    Uri.parse(BOOK_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, editText.text.toString())
                        .appendQueryParameter(MAX_RESULTS, "10")
                        .appendQueryParameter(PRINT_TYPE, "books")
                        .build().toString()
                ).openConnection() as HttpURLConnection).also {
                    it.requestMethod = "GET"
                    it.connect()
                    BufferedReader(InputStreamReader(it.inputStream)).apply {
                        val items = JSONObject(readText()).getJSONArray("items")
                        for (i in 0 until items.length()) {
                            items.getJSONObject(i).getJSONObject("volumeInfo").also { book ->
                                try {
                                    val title = book.getString("title")
                                    val author = book.getString("authors")
                                    results.add(title to author)
                                } catch (e: Exception) {}
                            }
                        }
                    }
                }.disconnect()

                runOnUiThread {
                    loader.isVisible = false
                    bookInfo.text = results.joinToString("\n")
                }
            }
        }
    }
}
