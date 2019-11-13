package com.example.cache.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.inmotionsoftware.promisekt.DeferredPromise
import com.inmotionsoftware.promisekt.Promise
import okhttp3.*
import java.io.IOException

class RemoteService(private val context: Context) : DataService {

    override fun getData(): Promise<Pair<RetrievalMethod, String>> =
        DeferredPromise<Pair<RetrievalMethod, String>>().let { promise ->
            val sharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
            val request = Request
                .Builder()
                .url("https://cat-fact.herokuapp.com/facts")
                .build()

            OkHttpClient()
                .newCall(request)
                .enqueue(CatFactCallback(promise, sharedPreferences))

            return promise.promise
        }

    inner class CatFactCallback(
        private val promise: DeferredPromise<Pair<RetrievalMethod, String>>,
        private val sharedPreferences: SharedPreferences
    ) : Callback {

        override fun onFailure(call: Call, e: IOException) {
            Log.e("Network request failure", "$e")
            promise.reject(e)
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response
                .body
                ?.string()
                ?: throw Exception("Empty response body from server.")

            with(sharedPreferences.edit()) {
                putString("Data_Key", responseBody)
                apply()
            }
            promise.resolve(Pair(RetrievalMethod.REMOTE, responseBody))
        }
    }
}