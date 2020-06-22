package com.example.flightmobileapp

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var isConnected = false;
    private var viewModel: LocalHostsViewModel? = null
    private var db: LocalHostsRoomDatabase? = null
    private var localHostDao: LocalHostDao? = null
    private var localHostsList : ArrayList<TextView> = ArrayList()
    private lateinit var urlInput: EditText
    private var client = Client(this)

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
        val listFromDataBase : List<LocalHost>? =  viewModel?.getAllList(localHostDao)
        var i : Int = 0
        if (listFromDataBase != null) {
            for (localHost in listFromDataBase) {
                localHostsList[i].text = localHost.localHost
                i++
            }
        }
    }

    /** Called when the user taps the connect button */
    fun gotoControl(view: View) {
        // try to connect to server with the given url:
        val urlString = urlInput.text.toString()
        val connected = client.connect(urlString)
        if (connected == 0) {
            // failed connecting to server:
            client.showError("Can't connect, try again!")
            urlInput.setText("")
        } else {
            // url is valid, try to get an image from server:
            client.createAPI()
            client.getAPI().getImg().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 404) {
                        client.showError("Can't connect to server, try again!")
                        urlInput.setText("")
                        return
                    }
                    // succeeded connecting to server:
                    viewModel?.insert(LocalHost(localHost = urlInput.text.toString()), localHostDao)
                    val intent = Intent(this@MainActivity, ControlsActivity::class.java)
                    intent.putExtra("url", urlString)
                    startActivity(intent)
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    client.showError("Can't connect to server, try again!")
                    urlInput.setText("")
                    return
                }
            })
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