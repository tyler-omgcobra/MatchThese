package org.omgcobra.matchthese.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemTagJoin
import org.omgcobra.matchthese.model.Tag

@Database(entities = [Item::class, ItemTagJoin::class, Tag::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "appDatabase")
                    .addMigrations(MIGRATION_1_2)
                    .build()
        }

        private val MIGRATION_1_2 = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ItemTagJoin RENAME TO ItemTagJoinOld")
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS ItemTagJoin(
                        `tagid` INTEGER,
                        `tagname` TEXT,
                        `itemid` INTEGER NOT NULL,
                        `itemname` TEXT NOT NULL,
                        PRIMARY KEY(`itemid`),
                        FOREIGN KEY(`tagid`) REFERENCES `Tag`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
                        FOREIGN KEY(`itemid`) REFERENCES `Item`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
                    )
                """.trimIndent())
                database.execSQL("""
                    INSERT INTO ItemTagJoin(tagid, tagname, itemid, itemname)
                    SELECT tagid, tagname, itemid, itemname
                    FROM ItemTagJoinOld
                """.trimIndent())
                database.execSQL("DROP TABLE ItemTagJoinOld")
            }
        }
    }

    abstract fun itemDao(): ItemDao
}