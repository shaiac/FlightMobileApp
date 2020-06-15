package com.example.flightmobileapp

import android.util.Log
import com.google.gson.GsonBuilder

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL


class Client : Serializable {
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



    fun sendJson() {
        con.requestMethod = "POST"
        con.setRequestProperty("Content-Type", "application/json; utf-8")
        con.setRequestProperty("Accept", "application/json")
        con.doOutput = true;
        // create json and send it to server
        val jsonParam = JSONObject()
        jsonParam.put("aileron", aileron.toString())
        jsonParam.put("rudder", rudder.toString())
        jsonParam.put("elevator", elevator.toString())
        jsonParam.put("throttle", throttle.toString())
        val json: String = "aileron: " + aileron.toString() + ",rudder: " + rudder.toString() +
                ",elevator: " + elevator.toString() + ",throttle: " + throttle.toString()

        val rb: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(urlConn.toString())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(Api::class.java)
        val body = api.post(rb).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    Log.d("FlightMobileApp", response.body().toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })


        // send to server:
        /* con.outputStream.use { os ->
            val input: ByteArray = jsonString.toByteArray()
            os.write(input, 0, input.size)
        }
        // get response:
        BufferedReader(
            InputStreamReader(con.inputStream, "utf-8")
        ).use { br ->
            val response = StringBuilder()
            var responseLine: String? = null
            while (br.readLine().also { responseLine = it } != null) {
                response.append(responseLine!!.trim { it <= ' ' })
            }
            println(response.toString())
        }
    }(*/
    }
}