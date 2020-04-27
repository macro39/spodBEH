package com.example.spodbeh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spodbeh.model.Runner
import com.example.spodbeh.net.callRest
import com.example.spodbeh.net.fromJson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import java.net.ConnectException

class LoginActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        if (editText_login_username.text.trim().isEmpty() || editText_login_password.text.trim().isEmpty()) {
            Toast.makeText(this, "Prosim, vyplňte polia!", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar_login.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = callRest("/runner/authorize/" + editText_login_username.text + "/" + editText_login_password.text,
                        "GET", null)
                Log.d(TAG, response)

                val data = GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                        .create().fromJson<Runner>(response)

                DataHolder.loggedUser = data

                if (DataHolder.loggedUser == null) {
                    GlobalScope.launch(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Nesprávne prihlasovacie údaje!", Toast.LENGTH_SHORT).show()
                        progressBar_login.visibility = View.GONE
                    }
                    return@launch
                } else {
                    GlobalScope.launch(Dispatchers.Main) {
                        progressBar_login.visibility = View.GONE
                        val intent = Intent(this@LoginActivity, BottomNavActivity::class.java)
                        startActivity(intent)
                    }
                }
            } catch (e: ConnectException) {
                e.printStackTrace()
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Źiadne internetové pripojenie!", Toast.LENGTH_SHORT).show()
                    progressBar_login.visibility = View.GONE
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Chyba pri parsovaní údajov!", Toast.LENGTH_SHORT).show()
                    progressBar_login.visibility = View.GONE
                }
            }
        }
    }
}