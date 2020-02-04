package org.omgcobra.matchthese.dao

import android.os.AsyncTask
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.model.*
import java.math.BigDecimal

class AppRepository {
    companion object {
        val db = MatchTheseApplication.getDB()
        private val recipeIngredientJoinDao = db.recipeIngredientJoinDao()
        private val recipeIngredientCompositeDao = db.recipeIngredientCompositeDao()
        private val allRecipesWithIngredients = recipeIngredientCompositeDao.allRecipesWithIngredients()
        private val allIngredientsWithRecipes = recipeIngredientCompositeDao.allIngredientsWithRecipes()
        private val allPantryItems = db.ingredientDao().getPantry()

        fun getRecipesWithIngredients() = allRecipesWithIngredients
        fun getIngredientsWithRecipes() = allIngredientsWithRecipes
        fun getPantry() = allPantryItems

        fun removeIngredientFromRecipe(recipe: CompositeNamedListEntity<Recipe, Ingredient>, ingredientName: String) = doAsync {
            recipe.joinList
                    .filter { recipeIngredientJoin -> recipeIngredientJoin.ingredient?.name == ingredientName }
                    .forEach { recipeIngredientJoinDao.delete(it) }
        }
        fun removeRecipeFromIngredient(ingredient: CompositeNamedListEntity<Ingredient, Recipe>, recipeName: String) = doAsync {
            ingredient.joinList
                    .filter { recipeIngredientJoin -> recipeIngredientJoin.recipe.name == recipeName }
                    .forEach { recipeIngredientJoinDao.delete(it) }
        }

        fun ensureIngredientInRecipe(recipe: CompositeNamedListEntity<Recipe, Ingredient>,
                                      ingredientName: String,
                                      amount: BigDecimal,
                                      unit: String) = doAsync { createOrUpdateJoin(recipe.entity, getOrCreate(ingredientName), amount, unit) }
        fun ensureRecipeHasIngredient(ingredient: CompositeNamedListEntity<Ingredient, Recipe>,
                                      recipeName: String,
                                      amount: BigDecimal,
                                      unit: String) = doAsync { createOrUpdateJoin(getOrCreate(recipeName), ingredient.entity, amount, unit) }
        inline fun <reified E: AbstractEntity<E>> getAll() = db.dao<E>().getAll()
        inline fun <reified E: AbstractEntity<E>> deleteAll() = asyncWithDao<E> { dao -> dao.deleteAll() }
        inline fun <reified E: AbstractEntity<E>> insert(entity: E) = asyncWithDao<E> { dao -> entity.id = dao.insert(entity) }
        inline fun <reified E: AbstractEntity<E>> update(entity: E) = asyncWithDao<E> { dao -> dao.update(entity) }
        inline fun <reified E: AbstractEntity<E>> delete(entity: E) = asyncWithDao<E> { dao -> dao.delete(entity) }

        private inline fun <reified E: NamedEntity<E>> getOrCreate(name: String): E {
            val entity = db.namedDao<E>().getByName(name) ?: NamedEntity.create(name)

            if (entity.id == 0L) {
                entity.id = db.namedDao<E>().insert(entity)
            }

            return entity
        }

        private fun createOrUpdateJoin(recipe: Recipe, ingredient: Ingredient, amount: BigDecimal, unit: String) {
            when (val byRecipeAndIngredient = recipeIngredientJoinDao.getByRecipeAndIngredient(recipe.id, ingredient.id)) {
                null -> recipeIngredientJoinDao.insert(RecipeIngredientJoin(recipe, ingredient, amount, unit))
                else -> {
                    byRecipeAndIngredient.amount = amount
                    byRecipeAndIngredient.unit = unit
                    recipeIngredientJoinDao.update(byRecipeAndIngredient)
                }
            }
        }

        private fun doAsync(handler: () -> Unit) {
            class VoidVoidVoidAsyncTask : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg params: Void?): Void? {
                    handler()
                    return null
                }
            }

            VoidVoidVoidAsyncTask().execute()
        }

        inline fun <reified E: AbstractEntity<E>> asyncWithDao(noinline handler: (dao: AbstractDao<E>) -> Unit) {
            object : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg params: Void?): Void? {
                    handler(db.dao())
                    return null
                }
            }.execute()
        }
    }
}
