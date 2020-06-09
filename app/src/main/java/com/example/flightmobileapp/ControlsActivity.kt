package com.example.flightmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.TextView
import android.widget.Toast

class ControlsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val bundle: Bundle? = intent.extras
        val id = bundle?.get("id_value")
        Toast.makeText(applicationContext,id.toString()+" "+id, Toast.LENGTH_LONG).show()
        

//        // Get the Intent that started this activity and extract the string
//        val message = intent.getStringExtra(EXTRA_MESSAGE)
//
//        // Capture the layout's TextView and set the string as its text
//        val textView = findViewById<TextView>(R.id.textView).apply {
//            text = message
//        }
    }
}