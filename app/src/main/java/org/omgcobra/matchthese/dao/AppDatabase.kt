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

@Database(entities = [Item::class, ItemTagJoin::class, Tag::class], version = 4)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "appDatabase")
                    .addMigrations(Migration1to2(), Migration2to3(), Migration3to4())
                    .build()
        }

        fun buildTest(context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        }
    }

    abstract fun itemDao(): ItemDao
    abstract fun itemWithTagsDao(): ItemWithTagsDao
    abstract fun tagDao(): TagDao
    abstract fun itemTagJoinDao(): ItemTagJoinDao
}

abstract class DatabaseMigration(from: Int, to: Int): Migration(from, to) {
    protected fun recreateTable(tableName: String, createStatement: String, columns: String, database: SupportSQLiteDatabase) {
        val oldName = tableName + "Old"
        database.execSQL("ALTER TABLE $tableName RENAME TO $oldName")
        database.execSQL(createStatement.trimIndent())
        database.execSQL("""
            INSERT INTO $tableName($columns)
            SELECT $columns
            FROM $oldName
        """.trimIndent())
        database.execSQL("DROP TABLE $oldName")
    }
}

class Migration1to2: DatabaseMigration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        recreateTable("ItemTagJoin", """
                    CREATE TABLE IF NOT EXISTS ItemTagJoin(
                        `tagid` INTEGER,
                        `tagname` TEXT,
                        `itemid` INTEGER NOT NULL,
                        `itemname` TEXT NOT NULL,
                        PRIMARY KEY(`itemid`),
                        FOREIGN KEY(`tagid`) REFERENCES `Tag`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
                        FOREIGN KEY(`itemid`) REFERENCES `Item`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
                    )
                """, "tagid, tagname, itemid, itemname", database)
    }
}

class Migration2to3: DatabaseMigration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE UNIQUE INDEX `index_Item_id_name` ON `Item` (`id`, `name`)")
        database.execSQL("CREATE UNIQUE INDEX `index_Tag_id_name` ON `Tag` (`id`, `name`)")
        recreateTable("ItemTagJoin", """
                    CREATE TABLE IF NOT EXISTS ItemTagJoin(
                        `id` INTEGER NOT NULL,
                        `tagname` TEXT,
                        `tagid` INTEGER,
                        `itemname` TEXT NOT NULL,
                        `itemid` INTEGER NOT NULL,
                        PRIMARY KEY(`itemid`),
                        FOREIGN KEY(`tagid`, `tagname`) REFERENCES `Tag`(`id`, `name`) ON UPDATE CASCADE ON DELETE CASCADE ,
                        FOREIGN KEY(`itemid`, `itemname`) REFERENCES `Item`(`id`, `name`) ON UPDATE CASCADE ON DELETE CASCADE
                    )
                """, "tagid, tagname, itemid, itemname", database)
    }
}

class Migration3to4: DatabaseMigration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        recreateTable("ItemTagJoin", """
                    CREATE TABLE IF NOT EXISTS ItemTagJoin(
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `tagname` TEXT,
                        `tagid` INTEGER,
                        `itemname` TEXT NOT NULL,
                        `itemid` INTEGER NOT NULL,
                        FOREIGN KEY(`tagid`, `tagname`) REFERENCES `Tag`(`id`, `name`) ON UPDATE CASCADE ON DELETE CASCADE ,
                        FOREIGN KEY(`itemid`, `itemname`) REFERENCES `Item`(`id`, `name`) ON UPDATE CASCADE ON DELETE CASCADE
                    )
                """, "tagid, tagname, itemid, itemname", database)
    }
}
