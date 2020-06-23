package com.example.flightmobileapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ramotion.fluidslider.FluidSlider
import kotlinx.android.synthetic.main.activity_controls.aileronText
import kotlinx.android.synthetic.main.activity_controls.elevatorText
import kotlinx.android.synthetic.main.activity_second.joystickView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.RoundingMode

class ControlsActivity : AppCompatActivity() {
    private var getImage = false
    private var client = Client(this)
    private var lastAileron = 0.0
    private var lastElevator = 0.0
    private var lastThrottle = 0.0
    private var lastRudder = 0.0
    lateinit var image : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("url")
        val connected = client.connect(url!!)
        if (connected == 1) {
            setContentView(R.layout.activity_second)
            setJoystick()
            setSliders()
            image = findViewById(R.id.image1)
            getImage = true
            getImage()
        }
    }

    override fun onStart() {
        super.onStart()
        getImage = true
        getImage()
    }

    override fun onDestroy() {
        getImage = false
        super.onDestroy()
    }

    private fun getImage() {
        CoroutineScope(IO).launch {
            while(getImage) {
                client.getImage(image)
                delay(300)
            }
        }
    }

    override fun onStop(){
        this.getImage= false;
        super.onStop()
    }

    override fun onResume(){
        super.onResume()
        this.getImage=true;
        getImage()
    }

    override fun onPause(){
        this.getImage=false;
        super.onPause()
    }

    @SuppressLint("SetTextI18n")
    private fun setJoystick() {
        val joystick = joystickView.right
        var changed = false
        joystickView.setOnMoveListener {
            angle, strength ->
            val aileron = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevator = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100
            // show values on screen:
            aileronText.setText("aileron: $aileron")
            elevatorText.setText("elevator: $elevator")
            // check if values changed in more than 1%:
            if ((aileron > 1.01 * lastAileron) || (aileron < 0.99 * lastAileron)) {
                client.setAileron(aileron)
                changed = true
                lastAileron = aileron
            }
            if ((elevator> 1.01 * lastElevator) || (elevator < 0.99 * lastElevator)) {
                client.setElevator(elevator)
                changed = true
                lastElevator = elevator
            }
            if (changed) {
                changed = false
                // send aileron and elevator values to server:
                client.sendJson()
            }
        }
    }

    private fun setSliders() {
        // set rudder slider (seek bar)
        val rudderSeek = findViewById<FluidSlider>(R.id.fluidSliderRudder)
        rudderSeek.bubbleText = "0"
        val maxR = 1
        val minR = -1
        val totalR = maxR - minR
        rudderSeek.positionListener = { p -> val rudder = minR + (totalR * p)
            // send rudder to server
            val rudderDisplay = rudder.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
            rudderSeek.bubbleText = rudderDisplay.toString()
            // check if value changed in more than 1% and send to server:
            if ((rudder > 1.01 * lastRudder) || (rudder < 0.99 * lastRudder)) { client.setRudder(rudder.toDouble())
                client.sendJson()
                lastRudder = rudder.toDouble()
            } }
        // set throttle slider (seek bar)
        val throttleSeek = findViewById<FluidSlider>(R.id.fluidSliderThrottle)
        val maxT = 1
        val minT = 0
        val totalT = maxT - minT
        throttleSeek.positionListener = { p -> val throttle = minT + (totalT * p)
            // send throttle to server
            val throttleDisplay = throttle.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
            throttleSeek.bubbleText = throttleDisplay.toString()
            // check if value changed in more than 1% and send to server:
            if ((throttle > 1.01 * lastThrottle) || (throttle < 0.99 * lastThrottle)) {
                client.setThrottle(throttle.toDouble())
                client.sendJson()
                lastThrottle = throttle.toDouble()
            }
        }
    }
}
