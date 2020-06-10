package com.example.flightmobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var localhost1: TextView
    private lateinit var localhost2: TextView
    private lateinit var localhost3: TextView
    private lateinit var localhost4: TextView
    private lateinit var localhost5: TextView
    private lateinit var urlInput: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    fun initViews() {
        this.urlInput = findViewById(R.id.editTextTextPostalAddress)
        this.localhost1 = findViewById(R.id.localhost1)
        this.localhost2 = findViewById(R.id.localhost2)
        this.localhost3 = findViewById(R.id.localhost3)
        this.localhost4 = findViewById(R.id.localhost4)
        this.localhost5 = findViewById(R.id.localhost5)
    }

    /** Called when the user taps the connect button */
    fun gotoControl(view: View) {
        val id:Int = 10 //send "10" to controls
        val intent = Intent(this, ControlsActivity::class.java)
        intent.putExtra("id_value", id)
        startActivity(intent)
    }

    fun putUrl(view : View) {
        when (view.id) {
            R.id.localhost1 -> urlInput.setText(localhost1.text)
            R.id.localhost2 -> urlInput.setText(localhost2.text)
            R.id.localhost3 -> urlInput.setText(localhost3.text)
            R.id.localhost4 -> urlInput.setText(localhost4.text)
            else -> urlInput.setText(localhost5.text)
        }


    }
}