package org.omgcobra.matchthese.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import org.jetbrains.anko.doAsync
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.model.Item

class ItemSaveDialogFragment: DialogFragment() {

    lateinit var itemName: String

    companion object {
        fun newInstance(name: String): ItemSaveDialogFragment {
            val fragment = ItemSaveDialogFragment()

            fragment.itemName = name

            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        val view = activity!!.layoutInflater.inflate(R.layout.fragment_edit_item, null)
        val itemNameEditText = view.findViewById<EditText>(R.id.item_name)
        itemNameEditText.setText(itemName)

        alertDialogBuilder.setView(view)
                .setTitle(getString(R.string.dialog_item_title))
                .setPositiveButton(R.string.save) { dialog, _ ->
                    saveItem(itemNameEditText.text.toString())
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }

        return alertDialogBuilder.create()
    }

    private fun saveItem(name: String) {
        if (name.isEmpty()) {
            return
        }

        ItemRepository().insertItem(Item(name))
    }
}