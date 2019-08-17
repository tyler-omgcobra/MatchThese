package org.omgcobra.matchthese.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.omgcobra.matchthese.model.*
import kotlin.reflect.KClass

@Database(entities = [Item::class, ItemTagJoin::class, Tag::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun itemTagCompositeDao(): ItemTagCompositeDao
    abstract fun tagDao(): TagDao
    abstract fun itemTagJoinDao(): ItemTagJoinDao
    fun <T : AbstractEntity> dao(klass: KClass<T>): AbstractDao<T> {
        return (when (klass) {
            Item::class -> itemDao()
            Tag::class -> tagDao()
            ItemTagJoin::class -> itemTagJoinDao()
            else -> throw IllegalArgumentException()
        }) as AbstractDao<T>
    }

    companion object {
        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "appDatabase")
                    .addMigrations(*migrations)
                    .build()
        }

        fun buildTest(context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        }

        private val migrations = arrayOf(
                object: DatabaseMigration(1, 2) {
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
                },
                object: DatabaseMigration(2, 3) {
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
                },
                object: DatabaseMigration(3, 4) {
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
        )
    }
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
