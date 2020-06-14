package com.example.flightmobileapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.ramotion.fluidslider.FluidSlider
import kotlinx.android.synthetic.main.activity_controls.*
import kotlinx.android.synthetic.main.activity_second.joystickView
import java.math.RoundingMode

class ControlsActivity : AppCompatActivity() {
    private lateinit var client: Client
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controls)
       // client = intent.extras?.get("client") as Client

        setJoystick()
        setSliders()
    }

    private fun setJoystick() {
        val joystick = joystickView.right
        joystickView.setOnMoveListener {
            angle, strength ->
            val aileron = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevator = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100
            // show values on screen:
            aileronText.setText(aileron.toDouble().toString())
            elevatorText.setText(elevator.toDouble().toString())
            // send aileron and elevator values server:
            client.setAileron(aileron)
            client.setElevator(elevator)
            sendCommand()
        }
    }

    private fun sendCommand() {
        client.sendJson()
    }

    private fun setSliders() {
        // set rudder slider (seek bar)
        val rudderSeek = findViewById<FluidSlider>(R.id.fluidSliderRudder)
        rudderSeek.bubbleText = "0"
        val maxR = 1
        val minR = -1
        val totalR = maxR - minR
        rudderSeek.positionListener = { p ->
            val rudder = minR + (totalR * p)
            // send rudder to server
            val rudderDisplay = rudder.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
            rudderSeek.bubbleText = rudderDisplay.toString()
            client.setRudder(rudder.toDouble())
            sendCommand()
        }
        // set throttle slider (seek bar)
        val throttleSeek = findViewById<FluidSlider>(R.id.fluidSliderThrottle)
        val maxT = 1
        val minT = 0
        val totalT = maxT - minT
        throttleSeek.positionListener = { p ->
            val throttle = minT + (totalT * p)
            // send throttle to server
            val throttleDisplay = throttle.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
            throttleSeek.bubbleText = throttleDisplay.toString()
            client.setThrottle(throttle.toDouble())
            sendCommand()
        }
    }


}

