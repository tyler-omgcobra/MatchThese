package org.omgcobra.matchthese.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.omgcobra.matchthese.model.*
import kotlin.reflect.KClass

@Database(entities = [Recipe::class, RecipeIngredientJoin::class, Ingredient::class], version = 8)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun recipeIngredientCompositeDao(): RecipeIngredientCompositeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun recipeIngredientJoinDao(): RecipeIngredientJoinDao
    fun <T : AbstractEntity<T>> dao(klass: KClass<T>): AbstractDao<T> {
        return (when (klass) {
            Recipe::class -> recipeDao()
            Ingredient::class -> ingredientDao()
            RecipeIngredientJoin::class -> recipeIngredientJoinDao()
            else -> throw IllegalArgumentException()
        }) as AbstractDao<T>
    }

    companion object {
        fun build(context: Context) = Room
                .databaseBuilder(context, AppDatabase::class.java, "appDatabase")
                .addMigrations(*migrations)
                .build()

        fun buildTest(context: Context) = Room
                .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .build()

        private val migrations = arrayOf(
                object : DatabaseMigration(1, 2) {
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
                object : DatabaseMigration(2, 3) {
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
                object : DatabaseMigration(3, 4) {
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
                },
                object : DatabaseMigration(4, 5) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE UNIQUE INDEX index_ItemTagJoin_itemid_itemname ON ItemTagJoin(itemid, itemname)")
                        database.execSQL("CREATE UNIQUE INDEX index_ItemTagJoin_tagid_tagname ON ItemTagJoin(tagid, tagname)")
                    }
                },
                object : DatabaseMigration(5, 6) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("DROP INDEX index_ItemTagJoin_itemid_itemname")
                        database.execSQL("DROP INDEX index_ItemTagJoin_tagid_tagname")
                        database.execSQL("CREATE UNIQUE INDEX index_ItemTagJoin_itemid_itemname_tagid_tagname ON ItemTagJoin(itemid, itemname, tagid, tagname)")
                    }
                },
                object : DatabaseMigration(6, 7) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("""
                            CREATE TABLE IF NOT EXISTS RecipeIngredientJoin(
                                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                `ingredientname` TEXT,
                                `ingredientid` INTEGER,
                                `recipename` TEXT NOT NULL,
                                `recipeid` INTEGER NOT NULL,
                                FOREIGN KEY(`ingredientid`, `ingredientname`) REFERENCES `Tag`(`id`, `name`) ON UPDATE CASCADE ON DELETE CASCADE ,
                                FOREIGN KEY(`recipeid`, `recipename`) REFERENCES `Item`(`id`, `name`) ON UPDATE CASCADE ON DELETE CASCADE
                            )
                        """)
                        database.execSQL("CREATE UNIQUE INDEX index_RecipeIngredientJoin_recipeid_recipename_ingredientid_ingredientname ON RecipeIngredientJoin(recipeid, recipename, ingredientid, ingredientname)")
                        database.execSQL("""
                            INSERT INTO RecipeIngredientJoin(ingredientname, ingredientid, recipename, recipeid)
                            SELECT itemname, itemid, tagname, tagid
                            FROM ItemTagJoin
                        """)
                        database.execSQL("ALTER TABLE Item RENAME TO Recipe")
                        database.execSQL("ALTER TABLE Tag RENAME TO Ingredient")
                        database.execSQL("DROP TABLE ItemTagJoin")
                    }
                },
                object : DatabaseMigration(7, 8) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE RecipeIngredientJoin ADD COLUMN `amount` TEXT NOT NULL DEFAULT ''")
                    }
                }
        )
    }
}

abstract class DatabaseMigration(from: Int, to: Int) : Migration(from, to) {
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
