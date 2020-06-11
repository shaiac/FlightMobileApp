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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controls)
        setJoystick()
        setSliders()


//        val seek = findViewById<SeekBar>(R.id.throttleSlider)
//        seek?.setOnSeekBarChangeListener(object :
//            SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(
//                seek: SeekBar,
//                progress: Int, fromUser: Boolean
//            ) {
//
//                // write custom code for progress is changed
//            }
//
//            override fun onStartTrackingTouch(seek: SeekBar) {
//                val a = seek.progress
//                seekBarText3.setText(seek.progress.toString())
//                // write custom code for progress is started
//            }
//
//            override fun onStopTrackingTouch(seek: SeekBar) {
//                // write custom code for progress is stopped
//            }
//        })
//
//        val seek2 = findViewById<SeekBar>(R.id.rudderSlider)
//        seek2?.setOnSeekBarChangeListener(object :
//            SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(
//                seek2: SeekBar,
//                progress: Int, fromUser: Boolean
//            ) {
//
//                // write custom code for progress is changed
//            }
//
//            override fun onStartTrackingTouch(seek2: SeekBar) {
//                val a = seek2.progress
//                seekBarText.setText(seek2.progress.toString())
//                // write custom code for progress is started
//            }
//
//            override fun onStopTrackingTouch(seek: SeekBar) {
//                // write custom code for progress is stopped
//            }
//        })


    }

    private fun setJoystick() {
        val joystick = joystickView.right
        joystickView.setOnMoveListener {
            angle, strength ->
            val aileron = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevator = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100

            aileronText.setText(aileron.toDouble().toString())
            elevatorText.setText(elevator.toDouble().toString())

            // send aileron and elevator to command server
    }
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
        }
    }


}

