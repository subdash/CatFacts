package com.example.cache.service

import android.content.Context
import com.example.cache.CacheQueries
import com.example.cache.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver

object DBProxy {

    private var cacheQueries: CacheQueries? = null

    fun getCacheQueries(context: Context): CacheQueries? {
        if (this.cacheQueries == null) {

            val driver = AndroidSqliteDriver(Database.Schema, context, "test.db")
            val db = Database(driver)
            this.cacheQueries = db.cacheQueries
        }
        return this.cacheQueries
    }
}
