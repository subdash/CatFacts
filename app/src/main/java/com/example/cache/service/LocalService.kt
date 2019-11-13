package com.example.cache.service

import android.content.Context
import com.inmotionsoftware.promisekt.DeferredPromise
import com.inmotionsoftware.promisekt.Promise

class LocalService(private val context: Context) : DataService {

    override fun getData(): Promise<Pair<RetrievalMethod, String>> =
        DeferredPromise<Pair<RetrievalMethod, String>>().let { promise ->
            val sharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
            val sharedPreferencesKey = "Data_Key"

            sharedPreferences
                .getString(sharedPreferencesKey, "DEFAULT")
                ?.let { value -> promise.resolve(Pair(RetrievalMethod.LOCAL, value)) }

            return promise.promise
        }
}