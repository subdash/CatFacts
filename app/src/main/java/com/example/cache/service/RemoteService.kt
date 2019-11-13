package com.example.cache.service

import android.content.Context
import android.util.Log
import com.example.cache.Cache
import com.inmotionsoftware.promisekt.DeferredPromise
import com.inmotionsoftware.promisekt.Promise
import okhttp3.*
import java.io.IOException

class RemoteService(private val context: Context) : DataService {

    override fun getData(): Promise<Pair<RetrievalMethod, String>> =
        DeferredPromise<Pair<RetrievalMethod, String>>().let { promise ->
            val request = Request
                .Builder()
                .url("https://cat-fact.herokuapp.com/facts")
                .build()

            OkHttpClient()
                .newCall(request)
                .enqueue(CatFactCallback(promise))

            return promise.promise
        }

    inner class CatFactCallback(private val promise: DeferredPromise<Pair<RetrievalMethod, String>>) : Callback {

        override fun onFailure(call: Call, e: IOException) {
            Log.e("Network request failure", "$e")
            promise.reject(e)
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response
                .body
                ?.string()
                ?: throw Exception("Empty response body from server.")

            DBProxy
                .getCacheQueries(this@RemoteService.context)
                ?.insertIntoCache(cache = Cache.Impl(result = responseBody))

            promise.resolve(Pair(RetrievalMethod.REMOTE, responseBody))
        }
    }
}