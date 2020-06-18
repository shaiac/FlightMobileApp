package com.example.flightmobileapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_second.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var image1 : ImageView
    private var viewModel: LocalHostsViewModel? = null
    private var db: LocalHostsRoomDatabase? = null
    private var localHostDao: LocalHostDao? = null
    private var localHostsList : ArrayList<TextView> = ArrayList()
    private lateinit var urlInput: EditText
    private var client = Client()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }
    //init the view only one time
    private fun init() {
        db = LocalHostsRoomDatabase.getDatabase(context = this)
        localHostDao = db?.localHostDao()
        viewModel = ViewModelProvider(this).get(LocalHostsViewModel::class.java)
        viewModel?.updateListFromDataBase(localHostDao)
        this.urlInput = findViewById(R.id.editTextTextPostalAddress)
        initLocalHostsList()
    }

    private fun initLocalHostsList() {
        this.urlInput = findViewById(R.id.editTextTextPostalAddress)
        this.localHostsList.add(findViewById(R.id.localhost1))
        this.localHostsList.add(findViewById(R.id.localhost2))
        this.localHostsList.add(findViewById(R.id.localhost3))
        this.localHostsList.add(findViewById(R.id.localhost4))
        this.localHostsList.add(findViewById(R.id.localhost5))
        var listFromDataBase : List<LocalHost>? =  viewModel?.getAllList(localHostDao)
        var i : Int = 0
        if (listFromDataBase != null) {
            for (localHost in listFromDataBase) {
                localHostsList[i].text = localHost.localHost
                i++;
            }
        }
    }

    /** Called when the user taps the connect button */
    fun gotoControl(view: View) {
        // try to connect to server with the given url:
        val urlString = urlInput.text.toString()
        val connected = client.connect(urlString)
        if (connected == 0) {
            val text = "Can't connect, try again!"
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
            urlInput.setText("")
        } else {
            // succeeded connecting to server:
            viewModel?.insert(LocalHost(localHost = urlInput.text.toString()), localHostDao)
            val intent = Intent(this, ControlsActivity::class.java)
            intent.putExtra("url", urlString)
            startActivity(intent)

//            client.sendImg()
//            var data = intent.getByteArrayExtra("image")
//            val v = data?.size?.let { BitmapFactory.decodeByteArray(data,0,it) }
//            runOnUiThread {
//                image1.setImageBitmap(v)
//            }
        }
    }

    fun putUrl(view : View) {
        when (view.id) {
            R.id.localhost1 -> urlInput.setText(localHostsList[0].text)
            R.id.localhost2 -> urlInput.setText(localHostsList[1].text)
            R.id.localhost3 -> urlInput.setText(localHostsList[2].text)
            R.id.localhost4 -> urlInput.setText(localHostsList[3].text)
            else -> urlInput.setText(localHostsList[4].text)
        }
    }
}