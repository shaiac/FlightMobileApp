package com.example.flightmobileapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
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
import java.io.IOException
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException


class Client : AppCompatActivity() {
    private lateinit var urlConn: URL
    private lateinit var con: HttpURLConnection
    public lateinit var B: Bitmap
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


    fun getImage() {
        con.requestMethod = "GET"

    }

    fun sendImg(){
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val str = urlConn.toString()
        val retrofit = Retrofit.Builder()
            .baseUrl(str)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(Api::class.java)
        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val intent = Intent(this@Client, MainActivity::class.java).apply{
                    putExtra("image", response.body()!!.bytes())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
    })
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
        //val json: String = "aileron: " + aileron.toString() + ",rudder: " + rudder.toString() +
          //      ",elevator: " + elevator.toString() + ",throttle: " + throttle.toString()
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
                var x = 1
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

    @Throws(JSONException::class)
    private fun buidJsonObject(): JSONObject {

        val jsonObject = JSONObject()
        jsonObject.accumulate("aileron", "0.5")
        jsonObject.accumulate("rudder", "0.5")
        jsonObject.accumulate("elevator", "0.5")
        jsonObject.accumulate("throttle", "0.5")
        return jsonObject
    }
}