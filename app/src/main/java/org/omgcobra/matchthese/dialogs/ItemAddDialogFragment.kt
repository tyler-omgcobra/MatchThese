package org.omgcobra.matchthese.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.model.Item
import java.lang.IllegalStateException

class ItemAddDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        return activity?.let {
            AlertDialog.Builder(it)
                    .setView(inflater.inflate(R.layout.fragment_edit_item, null))
                    .setPositiveButton(R.string.add_btn_text) { dialog, _ ->
                        dialog as Dialog
                        val editText: EditText = dialog.findViewById(R.id.item_edit_name)
                        ItemRepository().insertItem(Item(editText.text.toString()))
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}