package org.omgcobra.matchthese.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.omgcobra.matchthese.model.*

@Database(entities = [Recipe::class, RecipeIngredientJoin::class, Ingredient::class], version = 1)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun recipeIngredientCompositeDao(): RecipeIngredientCompositeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun recipeIngredientJoinDao(): RecipeIngredientJoinDao
    inline fun <reified T : NamedEntity<T>> namedDao(): NamedDao<T> {
        return (when (T::class) {
            Recipe::class -> recipeDao()
            Ingredient::class -> ingredientDao()
            else -> throw IllegalArgumentException()
        }) as NamedDao<T>
    }
    inline fun <reified T: AbstractEntity<T>> dao(): AbstractDao<T> {
        return (when (T::class) {
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

        private val migrations: Array<Migration> = arrayOf(
                object: DatabaseMigration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        // TODO stub when I need to migrate
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
