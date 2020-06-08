package com.example.flightmobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** Called when the user taps the connect button */
    fun gotoControl(view: View) {
        val id:Int = 10 //send "10" to controls
        val intent = Intent(this, ControlsActivity::class.java)
        intent.putExtra("id_value", id)
        startActivity(intent)
    }
}