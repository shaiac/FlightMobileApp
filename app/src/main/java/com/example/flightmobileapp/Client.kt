package com.example.flightmobileapp

import org.json.JSONObject
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL


class Client : Serializable {
    lateinit var url: URL
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
        var tempUrl = URL(url)
        var connection: HttpURLConnection? = null
        try {
            connection = tempUrl.openConnection() as HttpURLConnection
            // NOT FINISHED
        } catch (e: Exception) {
            return 0
        }
        return 1
    }

    fun sendJson() {
        // create json and send it to server
        val jsonParam = JSONObject()
        jsonParam.put("ID", "25")
        jsonParam.put("description", "Real")
        jsonParam.put("enable", "true")
        // https://stackoverflow.com/questions/13911993/sending-a-json-http-post-request-from-android
    }
}