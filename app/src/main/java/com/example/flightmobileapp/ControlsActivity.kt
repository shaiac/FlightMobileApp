package com.example.flightmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_controls.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_main.view.textView
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.activity_second.joystickView

class ControlsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controls)
        setJoystick()

        val seek = findViewById<SeekBar>(R.id.seekBar3)
        seek?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {

                // write custom code for progress is changed
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                val a = seek.progress
                seekBarText3.setText(seek.progress.toString())
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })

        val seek2 = findViewById<SeekBar>(R.id.seekBar)
        seek2?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek2: SeekBar,
                progress: Int, fromUser: Boolean
            ) {

                // write custom code for progress is changed
            }

            override fun onStartTrackingTouch(seek2: SeekBar) {
                val a = seek2.progress
                seekBarText.setText(seek2.progress.toString())
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })


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

