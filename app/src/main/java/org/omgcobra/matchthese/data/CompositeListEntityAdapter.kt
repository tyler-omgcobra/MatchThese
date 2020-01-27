package org.omgcobra.matchthese.data

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.fragments.Swipable
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.NamedEntity

abstract class CompositeListEntityAdapter<E: NamedEntity<E>, L: NamedEntity<L>>(internal val context: Context) : AbstractListEntityAdapter<CompositeNamedListEntity<E, L>, CompositeListEntityViewHolder>(), Swipable {

    protected abstract val editActionId: Int

    abstract fun getSubText(listEntity: CompositeNamedListEntity<E, *>): String
    abstract fun getImage(listEntity: CompositeNamedListEntity<E, *>): Int
    abstract fun getImageTint(listEntity: CompositeNamedListEntity<E, *>): Int
    abstract fun handleImageClick(listEntity: CompositeNamedListEntity<E, *>)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompositeListEntityViewHolder {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return CompositeListEntityViewHolder(inflater.inflate(R.layout.draggable_main_sub_content_view, parent, false))
    }

    override fun onBindViewHolder(holder: CompositeListEntityViewHolder, position: Int) {
        val listEntity = dataSet[position]
        holder.mainText.text = listEntity.toString()
        holder.subText.text = getSubText(listEntity)
        holder.img.setImageResource(getImage(listEntity))
        holder.img.imageTintList = ColorStateList.valueOf(getImageTint(listEntity))
        holder.img.setOnClickListener {
            handleImageClick(listEntity)
        }
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(editActionId, bundleOf("listEntity" to listEntity))
        }
    }
}
