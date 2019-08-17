package org.omgcobra.matchthese.tabs.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.model.Item

class ItemsViewModel(application: Application): AndroidViewModel(application) {
    private val itemsWithTagsLiveData = ItemRepository.getItemsWithTags()

    fun getItemsWithTagsList() = itemsWithTagsLiveData

    fun insertItem(item: Item) = ItemRepository.insert(item)
    fun deleteItem(item: Item) = ItemRepository.delete(item)
}