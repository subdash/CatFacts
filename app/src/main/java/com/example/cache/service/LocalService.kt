package com.example.cache.service

import android.content.Context
import com.inmotionsoftware.promisekt.DeferredPromise
import com.inmotionsoftware.promisekt.Promise

class LocalService(private val context: Context) : DataService {

    override fun getData(): Promise<Pair<RetrievalMethod, String>> =
        DeferredPromise<Pair<RetrievalMethod, String>>().let { promise ->
            DBProxy
                .getCacheQueries(this.context)
                ?.selectAllFromCache()
                ?.executeAsList()
                ?.let { promise.resolve(Pair(RetrievalMethod.LOCAL, it[0])) }

            return promise.promise
    }
}