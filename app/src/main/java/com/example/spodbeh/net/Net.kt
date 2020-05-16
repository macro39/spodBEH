package com.example.spodbeh.net

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.*
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONException
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


/**
 * Created by Kamil Macek on 25.4.2020.
 */

const val baseURL = "http://192.168.100.21:8080/spodbeh"

inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)

suspend fun callRest(endpoint: String, method: String, body: RequestBody?): String = suspendCancellableCoroutine { cont ->
    val client = OkHttpClient()
    val request = Request.Builder().method(method, body).url(baseURL + endpoint).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(request: Request, e: IOException) {
            cont.resumeWithException(e)
        }

        override fun onResponse(response: Response) {
            val responseData = response.body()?.string()
            try {
                cont.resume(responseData!!)
            } catch (e: JSONException) {
                e.printStackTrace()
                cont.resumeWithException(e)
            }
        }
    })
}