package com.example.flightmobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

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

    fun putUrl(view : View) {
        var urlInput: EditText = findViewById(R.id.editTextTextPostalAddress)
        var lochalhost1 : TextView = findViewById(R.id.localhost1)
        var lochalhost2 : TextView = findViewById(R.id.localhost2)
        var lochalhost3 : TextView = findViewById(R.id.localhost3)
        var lochalhost4 : TextView = findViewById(R.id.localhost4)
        var lochalhost5 : TextView = findViewById(R.id.localhost5)
        when (view.id) {
            R.id.localhost1 -> urlInput.setText(lochalhost1.text)
            R.id.localhost2 -> urlInput.setText(lochalhost2.text)
            R.id.localhost3 -> urlInput.setText(lochalhost3.text)
            R.id.localhost4 -> urlInput.setText(lochalhost4.text)
            else -> urlInput.setText(lochalhost5.text)
        }


    }
}