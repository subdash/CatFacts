package com.example.cache.service

import com.inmotionsoftware.promisekt.Promise

interface DataService {
    fun getData(): Promise<Pair<RetrievalMethod, String>>
}