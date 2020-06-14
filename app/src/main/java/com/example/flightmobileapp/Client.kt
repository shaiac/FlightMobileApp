package com.example.flightmobileapp

import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL


class Client : Serializable, Api {
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

    override fun getImg(): retrofit2.Call<ResponseBody> {
        TODO("Not yet implemented")
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
        val jsonString = "aileron: " + aileron.toString() + ",rudder: " + rudder.toString() +
                ",elevator: " + elevator.toString() + ",throttle: " + throttle.toString()
        // send to server:
        con.outputStream.use { os ->
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
    }
}