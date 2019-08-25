package org.omgcobra.matchthese.fragments.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.model.Item

class ItemWithTagsViewModel(application: Application): AndroidViewModel(application) {
    private val itemsWithTagsLiveData = ItemRepository.getItemsWithTags()

    fun getItemsWithTagsList() = itemsWithTagsLiveData

    fun insert(item: Item) = ItemRepository.insert(item)
    fun delete(item: Item) = ItemRepository.delete(item)
}