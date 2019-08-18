package org.omgcobra.matchthese

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.omgcobra.matchthese.dialogs.ItemAddDialogFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onAddItemBtnClick(view: View) {
        ItemAddDialogFragment().show(supportFragmentManager, "add_item")
    }

    fun onAddTagBtnClick(view: View) {

    }
}
