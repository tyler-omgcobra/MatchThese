package org.omgcobra.matchthese.dao

import android.os.AsyncTask
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.model.AbstractEntity
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemTagJoin
import org.omgcobra.matchthese.model.Tag

class ItemRepository {
    companion object {
        val db = MatchTheseApplication.getDB()
        private val itemWithTagsDao = db.itemTagCompositeDao()
        private val allItemsWithTags = itemWithTagsDao.allItemsWithTags()

        fun getItemsWithTags() = allItemsWithTags
        fun addTagToItem(item: Item, tag: Tag) = insert(ItemTagJoin(item, tag))
        inline fun <reified T: AbstractEntity> getAll() = db.dao(T::class).getAll()
        inline fun <reified T: AbstractEntity> insert(item: T) = InsertAsyncTask(db.dao(T::class)).execute(item)!!
        inline fun <reified T: AbstractEntity> update(item: T) = UpdateAsyncTask(db.dao(T::class)).execute(item)!!
        inline fun <reified T: AbstractEntity> delete(item: T) = DeleteAsyncTask(db.dao(T::class)).execute(item)!!
        inline fun <reified T: AbstractEntity> deleteAll() = DeleteAllAsyncTask(db.dao(T::class)).execute()!!
    }
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

class DeleteAllAsyncTask<T: AbstractEntity>(private val dao: AbstractDao<T>): AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg items: Void): Void? {
        dao.deleteAll()
        return null
    }
}
