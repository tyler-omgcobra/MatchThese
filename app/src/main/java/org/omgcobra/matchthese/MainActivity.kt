package org.omgcobra.matchthese

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import org.omgcobra.matchthese.data.AbstractListFragment
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        CompositeListEntityEditFragment.ListEntitySavedListener {
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupNavigation() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        drawerLayout = findViewById(R.id.drawer_layout)

        navigationView = findViewById(R.id.navigation_view)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
                setOf(R.id.recipeWithIngredientsFragment, R.id.ingredientWithRecipesFragment),
                drawerLayout
        )

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        toolbar.setupWithNavController(navController, appBarConfiguration)

        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true

        drawerLayout.closeDrawers()

        when (menuItem.itemId) {
            R.id.menu_recipes -> navController.navigate(R.id.recipeWithIngredientsFragment)
            R.id.menu_ingredients -> navController.navigate(R.id.ingredientWithRecipesFragment)
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun onAddRecipeBtnClick(view: View) {
        navController.navigate(R.id.edit_recipe)
    }

    fun onAddIngredientBtnClick(view: View) {
        navController.navigate(R.id.edit_ingredient)
    }

    override fun updateListData() {
        listOf(
                supportFragmentManager.findFragmentById(R.id.recipeWithIngredientsFragment) as AbstractListFragment<*, *, *>?,
                supportFragmentManager.findFragmentById(R.id.ingredientWithRecipesFragment) as AbstractListFragment<*, *, *>?
        ).forEach { it?.refreshListData() }
    }
}
