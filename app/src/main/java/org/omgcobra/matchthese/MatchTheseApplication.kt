package org.omgcobra.matchthese

import android.app.Application
import org.omgcobra.matchthese.dao.AppDatabase

class MatchTheseApplication: Application() {
    companion object {
        private lateinit var instance: MatchTheseApplication
        private lateinit var db: AppDatabase
        @Synchronized fun getInstance() = instance
        @Synchronized fun getDB() = db
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = AppDatabase.build(applicationContext)
    }

    fun test() {
        db = AppDatabase.buildTest(applicationContext)
    }
}
