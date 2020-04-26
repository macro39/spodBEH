package com.example.spodbeh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.spodbeh.model.Runner
import com.example.spodbeh.net.callRest
import com.example.spodbeh.net.fromJson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View?) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = callRest("/runner/authorize/kamil.macek/123456", "GET", null)
                Log.d(TAG, response)

                val data = GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                        .create().fromJson<Runner>(response)

                DataHolder.loggedUser = data
            } catch (e: Exception) {
                e.printStackTrace()
            }

            GlobalScope.launch(Dispatchers.Main) {
                val intent = Intent(this@LoginActivity, BottomNavActivity::class.java)
                startActivity(intent)
            }

        }
    }
}