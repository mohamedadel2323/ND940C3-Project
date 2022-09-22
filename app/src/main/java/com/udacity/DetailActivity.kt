package com.udacity

import android.content.Intent
import android.os.Bundle
import android.transition.Transition
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        file_name_value.text = intent.getStringExtra(MainActivity.FILE_NAME).toString()
        val state = intent.getBooleanExtra(MainActivity.DOWNLOAD_STATUS, false)
        if (state) {
            file_state_value.setTextColor(getColor(R.color.green))
            file_state_value.text = "Success"
        } else {
            file_state_value.setTextColor(getColor(R.color.red))
            file_state_value.text = "Fail"
        }
        backBtn.setOnClickListener {
            motion_layout.transitionToEnd()
            val intent = Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
            finish()
        }
    }

}
