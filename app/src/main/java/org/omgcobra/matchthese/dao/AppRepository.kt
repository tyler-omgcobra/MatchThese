package org.omgcobra.matchthese.dao

import android.os.AsyncTask
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.model.*

class AppRepository {
    companion object {
        val db = MatchTheseApplication.getDB()
        private val recipeIngredientCompositeDao = db.recipeIngredientCompositeDao()
        private val allRecipesWithIngredients = recipeIngredientCompositeDao.allRecipesWithIngredients()
        private val allIngredientsWithRecipes = recipeIngredientCompositeDao.allIngredientsWithRecipes()

        fun getRecipesWithIngredients() = allRecipesWithIngredients
        fun getIngredientsWithRecipes() = allIngredientsWithRecipes
        fun ensureIngredientInRecipe(recipe: CompositeNamedListEntity<Recipe, Ingredient>, ingredientName: String) = IngredientRecipeTask(db.recipeIngredientJoinDao(), db.ingredientDao(), ingredientName).execute(recipe)
        fun removeIngredientFromRecipe(recipe: CompositeNamedListEntity<Recipe, Ingredient>, ingredientName: String) = RemoveIngredientTask(db.recipeIngredientJoinDao(), db.ingredientDao(), ingredientName).execute(recipe)
        fun ensureRecipeHasIngredient(ingredient: CompositeNamedListEntity<Ingredient, Recipe>, recipeName: String) = RecipeIngredientTask(db.recipeIngredientJoinDao(), db.recipeDao(), recipeName).execute(ingredient)
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

class IngredientRecipeTask(private val recipeIngredientJoinDao: RecipeIngredientJoinDao, private val ingredientDao: IngredientDao, private val ingredientName: String): AsyncTask<CompositeNamedListEntity<Recipe, Ingredient>, Void, Void>() {
    override fun doInBackground(vararg recipesWithIngredients: CompositeNamedListEntity<Recipe, Ingredient>): Void? {
        val ingredient = ingredientDao.getByName(ingredientName) ?: Ingredient(ingredientName)

        if (ingredient.id == 0L) {
            ingredient.id = ingredientDao.insert(ingredient)
        }

        recipesWithIngredients.filter { !it.joinList.map { recipeIngredientJoin -> recipeIngredientJoin.ingredient!!.name }.contains(ingredientName) }
                .forEach { recipeIngredientJoinDao.insert(RecipeIngredientJoin(it.entity, ingredient, "")) }
        return null
    }
}

class RemoveIngredientTask(private val recipeIngredientJoinDao: RecipeIngredientJoinDao, private val ingredientDao: IngredientDao, private val ingredientName: String): AsyncTask<CompositeNamedListEntity<Recipe, Ingredient>, Void, Void>() {
    override fun doInBackground(vararg recipesWithIngredients: CompositeNamedListEntity<Recipe, Ingredient>): Void? {
        val ingredient = ingredientDao.getByName(ingredientName)
        recipesWithIngredients.filter { it.joinList.map { recipeIngredientJoin -> recipeIngredientJoin.ingredient!!.name }.contains(ingredientName) }
                .forEach { recipeIngredientJoinDao.delete(recipeIngredientJoinDao.getByRecipeAndIngredient(it.entity.id, ingredient!!.id)!!) }
        return null
    }
}

class RecipeIngredientTask(private val recipeIngredientJoinDao: RecipeIngredientJoinDao, private val recipeDao: RecipeDao, private val recipeName: String): AsyncTask<CompositeNamedListEntity<Ingredient, Recipe>, Void, Void>() {
    override fun doInBackground(vararg ingredientsWithRecipes: CompositeNamedListEntity<Ingredient, Recipe>): Void? {
        val recipe = recipeDao.getByName(recipeName) ?: Recipe(recipeName)

        if (recipe.id == 0L) {
            recipe.id = recipeDao.insert(recipe)
        }

        ingredientsWithRecipes.filter { !it.joinList.map { recipeIngredientJoin -> recipeIngredientJoin.recipe.name }.contains(recipeName) }
                .forEach { recipeIngredientJoinDao.insert(RecipeIngredientJoin(recipe, it.entity, "")) }
        return null
    }
}

class RemoveRecipeTask(private val recipeIngredientJoinDao: RecipeIngredientJoinDao, private val recipeDao: RecipeDao, private val recipeName: String): AsyncTask<CompositeNamedListEntity<Ingredient, Recipe>, Void, Void>() {
    override fun doInBackground(vararg ingredientsWithRecipes: CompositeNamedListEntity<Ingredient, Recipe>): Void? {
        val recipe = recipeDao.getByName(recipeName)
        ingredientsWithRecipes.filter { it.joinList.map { recipeIngredientJoin -> recipeIngredientJoin.recipe.name }.contains(recipeName) }
                .forEach { recipeIngredientJoinDao.delete(recipeIngredientJoinDao.getByRecipeAndIngredient(recipe!!.id, it.entity.id)!!) }
        return null
    }
}
