package org.omgcobra.matchthese.tabs.tag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.model.Tag

class TagWithItemsViewModel(application: Application): AndroidViewModel(application) {
    private val tagsWithItemsLiveData = ItemRepository.getTagsWithItems()

    fun getTagsWithItemsList() = tagsWithItemsLiveData

    fun insert(tag: Tag) = ItemRepository.insert(tag)
    fun delete(tag: Tag) = ItemRepository.delete(tag)
}