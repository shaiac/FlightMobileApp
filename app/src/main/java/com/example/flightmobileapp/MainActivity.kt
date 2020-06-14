package com.example.flightmobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import java.sql.ClientInfoStatus

class MainActivity : AppCompatActivity() {
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
        val connected = client.connect(urlInput.text.toString())
        if (connected == 0) {
            val text = "Can't connect, try again!"
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
            urlInput.setText("")
        } else {
            viewModel?.insert(LocalHost(localHost = urlInput.text.toString()), localHostDao)
            val intent = Intent(this, ControlsActivity::class.java)
            intent.putExtra("client", client)
            startActivity(intent)
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