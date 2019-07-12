package org.omgcobra.matchthese.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemTagJoin
import org.omgcobra.matchthese.model.Tag

@Database(entities = [Item::class, ItemTagJoin::class, Tag::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun itemDao(): ItemDao
}