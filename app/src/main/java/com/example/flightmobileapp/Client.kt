package com.example.flightmobileapp

import android.content.Context
import android.graphics.BitmapFactory
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import androidx.appcompat.app.AppCompatActivity

class Client(private var context: Context) : AppCompatActivity() {
    private lateinit var urlConn: URL
    private lateinit var con: HttpURLConnection
    private var aileron: Double = 0.0
    private var elevator: Double = 0.0
    private var throttle: Double = 0.0
    private var rudder: Double = 0.0
    private lateinit var api: Api

    fun setAileron(value: Double) {
        aileron = value
    }
    fun setElevator(value: Double) {
        elevator = value
    }
    fun setThrottle(value: Double) {
        throttle = value
    }
    fun setRudder(value: Double) {
        rudder = value
    }

    //in this func we try connect to the http address we got from user- if susses return 1 , else 0.
    fun connect(url: String):Int {
        val tempUrl: URL
        try {
            tempUrl = URL(url)
            //save the http address.
            con = tempUrl.openConnection() as HttpURLConnection
        } catch (e: Exception) {
            return 0
        }
        urlConn = tempUrl
        createAPI()
        return 1
    }

    //try to get the image from server.
    fun getImage(image : ImageView) {
          api.getImg().enqueue(object : Callback<ResponseBody> {
            //if we get response from server
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() != 200) {
                    showError("Can't get image from server")
                    return
                }
                val i = response.body()?.byteStream()
                val b = BitmapFactory.decodeStream(i)
                runOnUiThread {
                    image.setImageBitmap(b)
                }
            }
            //if we don't ger response - let the app knew.
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showError("Can't get image from server")
            }
        })
    }


    fun createAPI() {
        val json = GsonBuilder()
            .setLenient()
            .create()
        val url = urlConn.toString()
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(json))
            .build()
        api = retrofit.create(Api::class.java)
    }

    fun getAPI(): Api {
        return api
    }

    fun sendJson() {
        con.requestMethod = "POST"
        con.setRequestProperty("Content-Type", "application/json; utf-8")
        con.setRequestProperty("Accept", "application/json")
        con.doOutput = true
        // create json and send it to server
        val json =
            "{\"aileron\":$aileron,\n\"rudder\":$rudder,\n\"elevator\":$elevator,\n\"throttle\":$throttle\n}"
        val rb: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)
        api.post(rb).enqueue(object : Callback<ResponseBody> {
            //if we don't get response - let the app knew.
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showError("Server isn't responding")
                return
            }
            //if we get response from server
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.code() != 200) {
                        showError("Can't process values in server")
                    }
                } catch (e: IOException) {
                    showError("Can't process values in server")
                }
                return
            }
        })
    }

    fun showError(msg: String) {
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, msg, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}