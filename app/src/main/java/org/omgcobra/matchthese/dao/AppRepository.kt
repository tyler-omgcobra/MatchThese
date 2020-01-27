package org.omgcobra.matchthese.dao

import android.os.AsyncTask
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.model.*
import java.math.BigDecimal

class AppRepository {
    companion object {
        val db = MatchTheseApplication.getDB()
        private val ingredientDao = db.ingredientDao()
        private val recipeIngredientCompositeDao = db.recipeIngredientCompositeDao()
        private val allRecipesWithIngredients = recipeIngredientCompositeDao.allRecipesWithIngredients()
        private val allIngredientsWithRecipes = recipeIngredientCompositeDao.allIngredientsWithRecipes()
        private val allPantryItems = ingredientDao.getPantry()

        fun getRecipesWithIngredients() = allRecipesWithIngredients
        fun getIngredientsWithRecipes() = allIngredientsWithRecipes
        fun getPantry() = allPantryItems
        fun ensureIngredientInRecipe(recipe: CompositeNamedListEntity<Recipe, Ingredient>, ingredientName: String, amount: BigDecimal, unit: String) = IngredientRecipeTask(db.recipeIngredientJoinDao(), db.ingredientDao(), ingredientName, amount, unit).execute(recipe)
        fun removeIngredientFromRecipe(recipe: CompositeNamedListEntity<Recipe, Ingredient>, ingredientName: String) = RemoveIngredientTask(db.recipeIngredientJoinDao(), db.ingredientDao(), ingredientName).execute(recipe)
        fun ensureRecipeHasIngredient(ingredient: CompositeNamedListEntity<Ingredient, Recipe>, recipeName: String, amount: BigDecimal, unit: String) = RecipeIngredientTask(db.recipeIngredientJoinDao(), db.recipeDao(), recipeName, amount, unit).execute(ingredient)
        fun removeRecipeFromIngredient(ingredient: CompositeNamedListEntity<Ingredient, Recipe>, recipeName: String) = RemoveRecipeTask(db.recipeIngredientJoinDao(), db.recipeDao(), recipeName).execute(ingredient)
        inline fun <reified E: AbstractEntity<E>> getAll() = db.dao(E::class).getAll()
        inline fun <reified E: AbstractEntity<E>> deleteAll() = DeleteAllAsyncTask(db.dao(E::class)).execute()!!
        inline fun <reified E: AbstractEntity<E>> insert(entity: E) = InsertAsyncTask(db.dao(E::class)).execute(entity)!!
        inline fun <reified E: AbstractEntity<E>> update(entity: E) = UpdateAsyncTask(db.dao(E::class)).execute(entity)!!
        inline fun <reified E: AbstractEntity<E>> delete(entity: E) = DeleteAsyncTask(db.dao(E::class)).execute(entity)!!
    }
}

class InsertAsyncTask<E: AbstractEntity<E>>(private val dao: AbstractDao<E>): AsyncTask<E, Void, Void>() {
    override fun doInBackground(vararg entities: E): Void? {
        dao.insert(*entities).forEachIndexed { index, id -> entities[index].id = id }
        return null
    }
}

class UpdateAsyncTask<E: AbstractEntity<E>>(private val dao: AbstractDao<E>): AsyncTask<E, Void, Void>() {
    override fun doInBackground(vararg entities: E): Void? {
        dao.update(*entities)
        return null
    }
}

class DeleteAsyncTask<E: AbstractEntity<E>>(private val dao: AbstractDao<E>): AsyncTask<E, Void, Void>() {
    override fun doInBackground(vararg entities: E): Void? {
        dao.delete(*entities)
        return null
    }
}

class DeleteAllAsyncTask<E: AbstractEntity<E>>(private val dao: AbstractDao<E>): AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg entities: Void): Void? {
        dao.deleteAll()
        return null
    }
}

class IngredientRecipeTask(private val recipeIngredientJoinDao: RecipeIngredientJoinDao,
                           private val ingredientDao: IngredientDao,
                           private val ingredientName: String,
                           private val amount: BigDecimal,
                           private val unit: String): AsyncTask<CompositeNamedListEntity<Recipe, Ingredient>, Void, Void>() {
    override fun doInBackground(vararg recipesWithIngredients: CompositeNamedListEntity<Recipe, Ingredient>): Void? {
        val ingredient = ingredientDao.getByName(ingredientName) ?: Ingredient(ingredientName)

        if (ingredient.id == 0L) {
            ingredient.id = ingredientDao.insert(ingredient)
        }

        recipesWithIngredients.forEach {
            when (val byRecipeAndIngredient = recipeIngredientJoinDao.getByRecipeAndIngredient(it.entity.id, ingredient.id)) {
                null -> recipeIngredientJoinDao.insert(RecipeIngredientJoin(it.entity, ingredient, amount, unit))
                else -> {
                    byRecipeAndIngredient.amount = amount
                    byRecipeAndIngredient.unit = unit
                    recipeIngredientJoinDao.update(byRecipeAndIngredient)
                }
            }
        }
        return null
    }
}

class RemoveIngredientTask(private val recipeIngredientJoinDao: RecipeIngredientJoinDao, private val ingredientDao: IngredientDao, private val ingredientName: String): AsyncTask<CompositeNamedListEntity<Recipe, Ingredient>, Void, Void>() {
    override fun doInBackground(vararg recipesWithIngredients: CompositeNamedListEntity<Recipe, Ingredient>): Void? {
        val ingredient = ingredientDao.getByName(ingredientName)
        recipesWithIngredients.filter { it.joinList.any { recipeIngredientJoin -> recipeIngredientJoin.ingredient!!.name == ingredientName} }
                .forEach { recipeIngredientJoinDao.delete(recipeIngredientJoinDao.getByRecipeAndIngredient(it.entity.id, ingredient!!.id)!!) }
        return null
    }
}

class RecipeIngredientTask(private val recipeIngredientJoinDao: RecipeIngredientJoinDao,
                           private val recipeDao: RecipeDao,
                           private val recipeName: String,
                           private val amount: BigDecimal,
                           private val unit: String): AsyncTask<CompositeNamedListEntity<Ingredient, Recipe>, Void, Void>() {
    override fun doInBackground(vararg ingredientsWithRecipes: CompositeNamedListEntity<Ingredient, Recipe>): Void? {
        val recipe = recipeDao.getByName(recipeName) ?: Recipe(recipeName)

        if (recipe.id == 0L) {
            recipe.id = recipeDao.insert(recipe)
        }

        ingredientsWithRecipes.forEach {
            if (it.joinList.any { recipeIngredientJoin -> recipeIngredientJoin.recipe.name == recipeName }) {
                val byRecipeAndIngredient = recipeIngredientJoinDao.getByRecipeAndIngredient(recipe.id, it.entity.id)!!
                byRecipeAndIngredient.amount = amount
                byRecipeAndIngredient.unit = unit
                recipeIngredientJoinDao.update(byRecipeAndIngredient)
            } else {
                recipeIngredientJoinDao.insert(RecipeIngredientJoin(recipe, it.entity, amount, unit))
            }
        }

        return null
    }
}

class RemoveRecipeTask(private val recipeIngredientJoinDao: RecipeIngredientJoinDao, private val recipeDao: RecipeDao, private val recipeName: String): AsyncTask<CompositeNamedListEntity<Ingredient, Recipe>, Void, Void>() {
    override fun doInBackground(vararg ingredientsWithRecipes: CompositeNamedListEntity<Ingredient, Recipe>): Void? {
        val recipe = recipeDao.getByName(recipeName)
        ingredientsWithRecipes.filter { it.joinList.any { recipeIngredientJoin -> recipeIngredientJoin.recipe.name == recipeName} }
                .forEach { recipeIngredientJoinDao.delete(recipeIngredientJoinDao.getByRecipeAndIngredient(recipe!!.id, it.entity.id)!!) }
        return null
    }
}
