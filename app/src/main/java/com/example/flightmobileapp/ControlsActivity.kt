package com.example.flightmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_controls.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.activity_second.joystickView

class ControlsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controls)
        setJoystick()
        }

    private fun setJoystick() {
        val joystick = joystickView.right
        joystickView.setOnMoveListener {
            angle, strength ->
            val aileron = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevator = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100

            aileronText.setText((aileron.toDouble()).toString())
            elevatorText.setText(elevator.toDouble().toString())


            // send aileron and elevator to command server

    }
}



//        // Get the Intent that started this activity and extract the string
//        val message = intent.getStringExtra(EXTRA_MESSAGE)
//
//        // Capture the layout's TextView and set the string as its text
//        val textView = findViewById<TextView>(R.id.textView).apply {
//            text = message
//        }
}

