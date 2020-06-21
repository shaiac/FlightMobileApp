package com.example.flightmobileapp
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.ramotion.fluidslider.FluidSlider
import kotlinx.android.synthetic.main.activity_controls.aileronText
import kotlinx.android.synthetic.main.activity_controls.elevatorText
import kotlinx.android.synthetic.main.activity_second.joystickView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.RoundingMode

class ControlsActivity : AppCompatActivity() {
    private var client = Client(this)
    private var lastAileron = 0.0
    private var lastElevator = 0.0
    private var lastThrottle = 0.0
    private var lastRudder = 0.0
    lateinit var image : ImageView
    var begin = System.currentTimeMillis()
    var end = System.currentTimeMillis()
    var commError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("url")
        val connected = client.connect(url)
        if (connected == 1) {
            setContentView(R.layout.activity_second)
            setJoystick()
            setSliders()
            image = findViewById(R.id.image1)
          //  getImage(url)
        }
    }

    fun getImage(url : String) {
        Thread {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                val api = retrofit.create(Api::class.java)
            while(true) {
                val body = api.getImg().enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val I = response.body()?.byteStream()
                        val B = BitmapFactory.decodeStream(I)
                        runOnUiThread {
                            image.setImageBitmap(B)
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        val errorText = "Can't get image from server"
                        val toast = Toast.makeText(applicationContext, errorText, Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                    }
                })
                Thread.sleep(300)
            }
        }.start()
    }

    private fun setJoystick() {
        val joystick = joystickView.right
        var changed = false
        joystickView.setOnMoveListener {
            angle, strength ->
            val aileron = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevator = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100
            // show values on screen:
            aileronText.setText(aileron.toDouble().toString())
            elevatorText.setText(elevator.toDouble().toString())
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
             //   GlobalScope.launch { // launch new coroutine in background and continue
                   // val res =
                        client.sendJson()
                  //  checkSent(res)
              //  }
            }
        }
    }

    private fun checkSent(res: Int) {
        if (res < 0) {
//            end = System.currentTimeMillis()
            val errorText: String
//            if (end - begin > 10000) {
//                errorText = "server hasn't responded for 10 seconds," +
//                        "please return to previous screen"
//                begin = System.currentTimeMillis()
//            } else {
                errorText = "Can't send values to server"
//            }
            val toast = Toast.makeText(applicationContext, errorText, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
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
            // check if value changed in more than 1% and send to server:
            if ((rudder > 1.01 * lastRudder) || (rudder < 0.99 * lastRudder)) {
                client.setRudder(rudder.toDouble())
                client.sendJson()
                lastRudder = rudder.toDouble()
            }
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
            // check if value changed in more than 1% and send to server:
            if ((throttle > 1.01 * lastThrottle) || (throttle < 0.99 * lastThrottle)) {
                client.setThrottle(throttle.toDouble())
                client.sendJson()
                lastThrottle = throttle.toDouble()
            }
        }
    }
}

