package org.omgcobra.matchthese

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.contentView
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.dialogs.ItemAddDialogFragment
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.tabs.item.ItemsViewModel
import org.omgcobra.matchthese.tabs.item.OnListFragmentInteractionListener

class MainActivity : AppCompatActivity(), OnListFragmentInteractionListener {
    override fun onListClickItem(item: Item) {
        AlertDialog.Builder(applicationContext)
                .setView(contentView)
                .setTitle(item.name)
                .setMessage(item.id.toString())
                .setPositiveButton(R.string.cancel) {dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    override fun onListFragmentDeleteItem(item: Item) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onAddBtnClick(view: View) {
        ItemAddDialogFragment().show(supportFragmentManager, "add_item")
    }
}
