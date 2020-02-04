package org.omgcobra.matchthese

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import org.omgcobra.matchthese.data.AbstractListFragment
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.fragments.DrawerCloseOnBackListener
import org.omgcobra.matchthese.fragments.ExitToastCallback

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
                setOf(R.id.recipeWithIngredientsFragment, R.id.ingredientWithRecipesFragment, R.id.pantryFragment),
                drawerLayout
        )

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        toolbar.setupWithNavController(navController, appBarConfiguration)

        navigationView.setNavigationItemSelectedListener(this)

        drawerLayout.addDrawerListener(DrawerCloseOnBackListener(onBackPressedDispatcher, drawerLayout))

        onBackPressedDispatcher.addCallback(this, ExitToastCallback(baseContext))
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        drawerLayout.closeDrawers()

        val dest = when (menuItem.itemId) {
            R.id.menu_recipes -> R.id.recipeWithIngredientsFragment
            R.id.menu_ingredients -> R.id.ingredientWithRecipesFragment
            R.id.menu_pantry -> R.id.pantryFragment
            else -> return false
        }

        navController.popBackStack(R.id.nav_graph, false)
        navController.navigate(dest)

        menuItem.isChecked = true

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    fun onAddRecipeBtnClick(view: View) {
        navController.navigate(R.id.edit_recipe)
    }

    fun onAddIngredientBtnClick(view: View) {
        navController.navigate(R.id.edit_ingredient)
    }

    override fun updateListData() {
        listOf(
                supportFragmentManager.findFragmentById(R.id.recipeWithIngredientsFragment) as AbstractListFragment<*, *>?,
                supportFragmentManager.findFragmentById(R.id.ingredientWithRecipesFragment) as AbstractListFragment<*, *>?
        ).forEach { it?.refreshListData() }
    }
}
