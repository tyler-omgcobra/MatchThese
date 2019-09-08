package org.omgcobra.matchthese.test

import android.view.View
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.isA
import org.hamcrest.core.StringStartsWith
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.omgcobra.matchthese.MainActivity
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.R
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class RecipeBehaviorTest {

    private lateinit var name: String
    private lateinit var ingredient: String

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        name = UUID.randomUUID().toString()
        ingredient = UUID.randomUUID().toString()
    }

    @Test
    fun recipeTest() {
        addRecipe()
        checkIngredientHasRecipe()
        deleteRecipe()
        deleteIngredient()
    }

    private fun addRecipe() {
        recipeView()

        onView(withId(R.id.floating_action_button_recipe))
                .perform(click())

        onView(withId(R.id.edit_name))
                .check(matches(isDisplayed()))
                .perform(typeText(name))

        onView(withId(R.id.edit_list))
                .check(matches(isDisplayed()))
                .perform(typeText("$ingredient,"))

        onView(withId(R.id.action_save))
                .check(matches(isDisplayed()))
                .perform(click())
    }

    private fun checkIngredientHasRecipe() {
        ingredientView()

        onView(allOf(withId(R.id.main_text), withText("$ingredient (1)")))
                .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.sub_text), withText(name)))
                .check(matches(isDisplayed()))
    }

    private fun deleteRecipe() {
        recipeView()

        val mainTextWithName = allOf(withId(R.id.main_text), withText(name))
        onView(mainTextWithName)
                .check(matches(isDisplayed()))
                .perform(swipeLeft())

        recipeView()

        onView(mainTextWithName)
                .check(doesNotExist())
    }

    private fun deleteIngredient() {
        ingredientView()

        val mainTextStartsWith = allOf(withId(R.id.main_text), withText(StringStartsWith(ingredient)))
        onView(mainTextStartsWith)
                .check(matches(isDisplayed()))
                .perform(swipeLeft())

        ingredientView()

        onView(mainTextStartsWith)
                .check(doesNotExist())
    }

    private fun clickDrawerItem(textId: Int) {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
                .check(matches(DrawerMatchers.isOpen()))

        onView(allOf(withText(textId), isA(AppCompatCheckedTextView::class.java) as Matcher<View>))
                .perform(click())
    }

    private fun recipeView() = clickDrawerItem(R.string.recipes_title)
    private fun ingredientView() = clickDrawerItem(R.string.ingredients_title)

    @After
    fun cleanup() {
        val db = MatchTheseApplication.getDB()
        val ingredientDao = db.ingredientDao()
        val recipeDao = db.recipeDao()

        val recipe = recipeDao.getByName(name)
        recipe?.let { recipeDao.delete(it) }

        val ingredient = ingredientDao.getByName(ingredient)
        ingredient?.let { ingredientDao.delete(it) }
    }
}