package org.omgcobra.matchthese.data

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokenautocomplete.TokenCompleteTextView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.NamedEntity
import org.omgcobra.matchthese.model.Tag

fun getView(entity: NamedEntity<*>, context: Context, viewGroup: ViewGroup): View {
    val l = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = l.inflate(R.layout.entity_token, viewGroup, false) as TextView
    view.text = entity.toString()

    return view

}

class TagCompleteView(context: Context, attrs: AttributeSet): TokenCompleteTextView<Tag>(context, attrs) {
    override fun getViewForObject(tag: Tag) = getView(tag, context, parent as ViewGroup)
    override fun defaultObject(completionText: String) = Tag(completionText)
}

class ItemCompleteView(context: Context, attrs: AttributeSet): TokenCompleteTextView<Item>(context, attrs) {
    override fun getViewForObject(item: Item) = getView(item, context, parent as ViewGroup)
    override fun defaultObject(completionText: String) = Item(completionText)
}
