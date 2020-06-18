package com.example.flightmobileapp

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

class Client : AppCompatActivity() {
    private lateinit var urlConn: URL
    private lateinit var con: HttpURLConnection
    private var aileron: Double = 0.0
    private var elevator: Double = 0.0
    private var throttle: Double = 0.0
    private var rudder: Double = 0.0

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

    fun connect(url: String):Int {
        val tempUrl: URL
        try {
            tempUrl = URL(url)
            con = tempUrl.openConnection() as HttpURLConnection

        } catch (e: Exception) {
            return 0
        }
        urlConn = tempUrl
        return 1
    }

    fun sendJson(): Int {
        var statusCode = 0
        con.requestMethod = "POST"
        con.setRequestProperty("Content-Type", "application/json; utf-8")
        con.setRequestProperty("Accept", "application/json")
        con.doOutput = true;
        // create json and send it to server
        val json: String = "{\"aileron\":$aileron,\n\"rudder\":$rudder,\n\"elevator\":$elevator,\n\"throttle\":$throttle\n}"
        //val json = buidJsonObject()
        val rb: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val str = urlConn.toString()
        val retrofit = Retrofit.Builder()
            .baseUrl(str)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(Api::class.java)
        val body = api.post(rb).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                statusCode = 500
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.body().toString() != "200") {
                        statusCode = 404
                    }
                   // Log.d("FlightMobileApp", response.body().toString())
                } catch (e: IOException) {
                    statusCode = 400
                  //  e.printStackTrace()
                }
            }
        })
        return statusCode
    }
}