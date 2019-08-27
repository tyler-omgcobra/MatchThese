package org.omgcobra.matchthese.dao

import android.os.AsyncTask
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.model.*

class ItemRepository {
    companion object {
        val db = MatchTheseApplication.getDB()
        private val itemTagCompositeDao = db.itemTagCompositeDao()
        private val allItemsWithTags = itemTagCompositeDao.allItemsWithTags()
        private val allTagsWithItems = itemTagCompositeDao.allTagsWithItems()

        fun getItemsWithTags() = allItemsWithTags
        fun getTagsWithItems() = allTagsWithItems
        fun ensureTagOnItem(item: ItemWithTags, tagName: String) = TagItemTask(db.itemTagJoinDao(), db.tagDao(), tagName).execute(item)
        inline fun <reified T: AbstractEntity<T>> getAll() = db.dao(T::class).getAll()
        inline fun <reified T: AbstractEntity<T>> deleteAll() = DeleteAllAsyncTask(db.dao(T::class)).execute()!!
        inline fun <reified T: AbstractEntity<T>> insert(item: T) = InsertAsyncTask(db.dao(T::class)).execute(item)!!
        inline fun <reified T: AbstractEntity<T>> update(item: T) = UpdateAsyncTask(db.dao(T::class)).execute(item)!!
        inline fun <reified T: AbstractEntity<T>> delete(item: T) = DeleteAsyncTask(db.dao(T::class)).execute(item)!!
    }
}

class InsertAsyncTask<T: AbstractEntity<T>>(private val dao: AbstractDao<T>): AsyncTask<T, Void, Void>() {
    override fun doInBackground(vararg items: T): Void? {
        dao.insert(*items).forEachIndexed { index, id -> items[index].id = id }
        return null
    }
}

class UpdateAsyncTask<T: AbstractEntity<T>>(private val dao: AbstractDao<T>): AsyncTask<T, Void, Void>() {
    override fun doInBackground(vararg items: T): Void? {
        dao.update(*items)
        return null
    }
}

class DeleteAsyncTask<T: AbstractEntity<T>>(private val dao: AbstractDao<T>): AsyncTask<T, Void, Void>() {
    override fun doInBackground(vararg items: T): Void? {
        dao.delete(*items)
        return null
    }
}

class DeleteAllAsyncTask<T: AbstractEntity<T>>(private val dao: AbstractDao<T>): AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg items: Void): Void? {
        dao.deleteAll()
        return null
    }
}

class TagItemTask(private val itemTagJoinDao: ItemTagJoinDao, private val tagDao: TagDao, private val tagName: String): AsyncTask<ItemWithTags, Void, Void>() {
    override fun doInBackground(vararg itemsWithTags: ItemWithTags): Void? {
        val tag = tagDao.getByName(tagName) ?: Tag(tagName)

        if (tag.id == 0L) {
            tag.id = tagDao.insert(tag)
        }

        itemsWithTags.forEach {
            if (!it.list.contains(tagName)) {
                itemTagJoinDao.insert(ItemTagJoin(it.entity, tag))
            }
        }
        return null
    }
}
