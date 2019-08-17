package org.omgcobra.matchthese.dao

import android.os.AsyncTask
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.model.AbstractEntity
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemTagJoin
import org.omgcobra.matchthese.model.Tag

class ItemRepository {
    private val db = MatchTheseApplication.getDB()
    private val itemDao = db.itemDao()
    private val itemWithTagsDao = db.itemWithTagsDao()
    private val tagDao = db.tagDao()
    private val itemTagJoinDao = db.itemTagJoinDao()
    private val allItems = itemDao.allItems()
    private val allItemsWithTags = itemWithTagsDao.allItemsWithTags()
    private val allTags = tagDao.allTags()

    fun getItems() = allItems
    fun getItemsWithTags() = allItemsWithTags
    fun getTags() = allTags
    fun addTagToItem(item: Item, tag: Tag): AsyncTask<ItemTagJoin, Void, Void> = InsertAsyncTask(itemTagJoinDao).execute(ItemTagJoin(item, tag))
    fun removeItemTagJoin(itemTagJoin: ItemTagJoin): AsyncTask<ItemTagJoin, Void, Void> = DeleteAsyncTask(itemTagJoinDao).execute(itemTagJoin)
    fun insertItem(item: Item): AsyncTask<Item, Void, Void> = InsertAsyncTask(itemDao).execute(item)
    fun updateItem(item: Item): AsyncTask<Item, Void, Void> = UpdateAsyncTask(itemDao).execute(item)
    fun deleteItem(item: Item): AsyncTask<Item, Void, Void> = DeleteAsyncTask(itemDao).execute(item)
    fun deleteAllItems(): AsyncTask<Void, Void, Void> = DeleteAllAsyncTask(itemDao).execute()
}

class InsertAsyncTask<T: AbstractEntity>(private val dao: AbstractDao<T>): AsyncTask<T, Void, Void>() {
    override fun doInBackground(vararg items: T): Void? {
        dao.insert(*items).forEachIndexed { index, id -> items[index].id = id }
        return null
    }
}

class UpdateAsyncTask<T: AbstractEntity>(private val dao: AbstractDao<T>): AsyncTask<T, Void, Void>() {
    override fun doInBackground(vararg items: T): Void? {
        dao.update(*items)
        return null
    }
}

class DeleteAsyncTask<T: AbstractEntity>(private val dao: AbstractDao<T>): AsyncTask<T, Void, Void>() {
    override fun doInBackground(vararg items: T): Void? {
        dao.delete(*items)
        return null
    }
}

class DeleteAllAsyncTask(private val dao: ItemDao): AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg items: Void): Void? {
        dao.deleteAll()
        return null
    }
}
