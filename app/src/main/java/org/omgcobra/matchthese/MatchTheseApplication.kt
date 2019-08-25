package org.omgcobra.matchthese

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
}
