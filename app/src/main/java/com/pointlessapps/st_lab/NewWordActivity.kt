package com.pointlessapps.st_lab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_new_word.*

class NewWordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        button_save.setOnClickListener {
            if (TextUtils.isEmpty(edit_word.text)) {
                setResult(RESULT_CANCELED)
            } else {
                setResult(RESULT_OK, Intent().putExtra("reply", edit_word.text.toString()))
            }
            finish()
        }
    }
}
