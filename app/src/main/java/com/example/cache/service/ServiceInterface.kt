package com.example.cache.service

import android.content.Context
import com.inmotionsoftware.promisekt.Promise

class ServiceInterface(private val context: Context) : DataService {

    private val remoteService = RemoteService(this.context)
    private val localService = LocalService(this.context)

    override fun getData(): Promise<Pair<RetrievalMethod, String>> = when {
        this.dataIsCached() -> this.localService.getData()
        else                -> this.remoteService.getData()
    }

    private fun dataIsCached(): Boolean {
        val result = DBProxy
            .getCacheQueries(this.context)
            ?.selectAllFromCache()
            ?.executeAsList()

        return !result
            ?.firstOrNull()
            .isNullOrBlank()
    }
}