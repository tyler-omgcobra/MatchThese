package org.omgcobra.matchthese.test

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.*
import org.junit.runner.RunWith
import org.omgcobra.matchthese.model.Recipe
import java.io.IOException
import java.lang.Exception

import org.hamcrest.core.Is.`is`
import org.junit.*
import org.junit.runners.MethodSorters
import org.omgcobra.matchthese.dao.*
import org.omgcobra.matchthese.model.RecipeIngredientJoin
import org.omgcobra.matchthese.model.Ingredient

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.DEFAULT)
class DBReadWriteTest {
    private lateinit var recipeDao: RecipeDao
    private lateinit var ingredientDao: IngredientDao
    private lateinit var recipeIngredientJoinDao: RecipeIngredientJoinDao
    private lateinit var recipeIngredientCompositeDao: RecipeIngredientCompositeDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        db = AppDatabase.buildTest(ApplicationProvider.getApplicationContext())
        recipeDao = db.recipeDao()
        ingredientDao = db.ingredientDao()
        recipeIngredientJoinDao = db.recipeIngredientJoinDao()
        recipeIngredientCompositeDao = db.recipeIngredientCompositeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testCRUD() {
        val recipe = createRecipe("Tyler")
        val ingredient = createIngredient("cool")

        readRecipe(recipe)
        readIngredient(ingredient)

        joinRecipeAndIngredient(recipe, ingredient)

        checkJoin(recipe, ingredient)

        updateRecipe(recipe, "Stephanie")
        updateIngredient(ingredient, "awesome")

        checkJoin(recipe, ingredient)

        deleteIngredient(ingredient)
        assertThat(recipeIngredientCompositeDao.loadRecipeWithIngredients(recipe.id).joinList as Collection<*>, `is`(empty()))
        assertThat(recipeIngredientCompositeDao.loadIngredientWithRecipes(ingredient.id), nullValue())
        deleteItem(recipe)
    }

    private fun joinRecipeAndIngredient(recipe: Recipe, ingredient: Ingredient) {
        val id = recipeIngredientJoinDao.insert(RecipeIngredientJoin(recipe, ingredient, ""))
        assertThat(id, greaterThan(0L))
    }

    private fun checkJoin(recipe: Recipe, ingredient: Ingredient) {
        val recipeWithIngredients = recipeIngredientCompositeDao.loadRecipeWithIngredients(recipe.id)
        assertThat(recipeWithIngredients.entity, equalTo(recipe))
        assertThat(recipeWithIngredients.joinList.map { it.ingredient!!.name }, contains(ingredient.name))
    }

    private fun createRecipe(name: String): Recipe {
        val recipe = Recipe(name)
        val recipeId = recipeDao.insert(recipe)
        assertThat(recipeId, greaterThan(0L))
        recipe.id = recipeId
        return recipe
    }

    private fun createIngredient(name: String): Ingredient {
        val ingredient = Ingredient(name)
        val ingredientId = ingredientDao.insert(ingredient)
        assertThat(ingredientId, greaterThan(0L))
        ingredient.id = ingredientId
        return ingredient
    }

    private fun readRecipe(recipe: Recipe) {
        val byId = recipeDao.load(recipe.id)
        assertThat(byId, equalTo(recipe))

        val byName = recipeDao.getByName(recipe.name)
        assertThat(byName, equalTo(recipe))
    }

    private fun readIngredient(ingredient: Ingredient) {
        val byId = ingredientDao.load(ingredient.id)
        assertThat(byId, equalTo(ingredient))

        val byName = ingredientDao.getByName(ingredient.name)
        assertThat(byName, equalTo(ingredient))
    }

    private fun updateRecipe(recipe: Recipe, name: String) {
        val oldName = recipe.name
        recipe.name = name
        recipeDao.update(recipe)

        val byId = recipeDao.load(recipe.id)
        assertThat(byId, equalTo(recipe))

        val byName = recipeDao.getByName(oldName)
        assertThat(byName, nullValue())
    }

    private fun updateIngredient(ingredient: Ingredient, name: String) {
        val oldName = ingredient.name
        ingredient.name = name
        ingredientDao.update(ingredient)

        val byId = ingredientDao.load(ingredient.id)
        assertThat(byId, equalTo(ingredient))

        val byName = ingredientDao.getByName(oldName)
        assertThat(byName, nullValue())
    }

    private fun deleteItem(recipe: Recipe) {
        val id = recipe.id
        val name = recipe.name
        recipeDao.delete(recipe)

        val byId = recipeDao.load(id)
        assertThat(byId, nullValue())

        val byName = recipeDao.getByName(name)
        assertThat(byName, nullValue())

        assertThat(recipeIngredientCompositeDao.loadRecipeWithIngredients(recipe.id), nullValue())
    }

    private fun deleteIngredient(ingredient: Ingredient) {
        val id = ingredient.id
        val name = ingredient.name
        ingredientDao.delete(ingredient)

        val byId = ingredientDao.load(id)
        assertThat(byId, nullValue())

        val byName = ingredientDao.getByName(name)
        assertThat(byName, nullValue())
    }
}
