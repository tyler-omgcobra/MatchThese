package org.omgcobra.matchthese.dao

import android.os.AsyncTask
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.model.Item

class ItemRepository() {
    private val itemDao = MatchTheseApplication.getDB().itemDao()
    private val allData = itemDao.allItems()

    fun getAllData() = allData
    fun getItemsWithTags() = itemDao.allItemsWithTags()
    fun insertItem(item: Item): AsyncTask<Item, Void, Void> = InsertAsyncTask(itemDao).execute(item)
    fun updateItem(item: Item): AsyncTask<Item, Void, Void> = UpdateAsyncTask(itemDao).execute(item)
    fun deleteItem(item: Item): AsyncTask<Item, Void, Void> = DeleteAsyncTask(itemDao).execute(item)
    fun deleteAll(): AsyncTask<Void, Void, Void> = DeleteAllAsyncTask(itemDao).execute()
}

class InsertAsyncTask(private val dao: ItemDao): AsyncTask<Item, Void, Void>() {
    override fun doInBackground(vararg items: Item): Void? {
        dao.insertItems(*items)
        return null
    }
}

class UpdateAsyncTask(private val dao: ItemDao): AsyncTask<Item, Void, Void>() {
    override fun doInBackground(vararg items: Item): Void? {
        dao.updateItems(*items)
        return null
    }
}

class DeleteAsyncTask(private val dao: ItemDao): AsyncTask<Item, Void, Void>() {
    override fun doInBackground(vararg items: Item): Void? {
        dao.deleteItems(*items)
        return null
    }
}

class DeleteAllAsyncTask(private val dao: ItemDao): AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg items: Void): Void? {
        dao.deleteAll()
        return null
    }
}
