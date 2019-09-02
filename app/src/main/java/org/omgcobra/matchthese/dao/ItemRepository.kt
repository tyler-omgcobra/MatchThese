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
        fun ensureTagOnItem(item: CompositeNamedListEntity<Item, Tag>, tagName: String) = TagItemTask(db.itemTagJoinDao(), db.tagDao(), tagName).execute(item)
        fun removeTagFromItem(item: CompositeNamedListEntity<Item, Tag>, tagName: String) = RemoveTagTask(db.itemTagJoinDao(), db.tagDao(), tagName).execute(item)
        fun ensureItemInTag(tag: CompositeNamedListEntity<Tag, Item>, itemName: String) = ItemTagTask(db.itemTagJoinDao(), db.itemDao(), itemName).execute(tag)
        fun removeItemFromTag(tag: CompositeNamedListEntity<Tag, Item>, itemName: String) = RemoveItemTask(db.itemTagJoinDao(), db.itemDao(), itemName).execute(tag)
        inline fun <reified E: AbstractEntity<E>> getAll() = db.dao(E::class).getAll()
        inline fun <reified E: AbstractEntity<E>> deleteAll() = DeleteAllAsyncTask(db.dao(E::class)).execute()!!
        inline fun <reified E: AbstractEntity<E>> insert(entity: E) = InsertAsyncTask(db.dao(E::class)).execute(entity)!!
        inline fun <reified E: AbstractEntity<E>> update(entity: E) = UpdateAsyncTask(db.dao(E::class)).execute(entity)!!
        inline fun <reified E: AbstractEntity<E>> delete(entity: E) = DeleteAsyncTask(db.dao(E::class)).execute(entity)!!
    }
}

class InsertAsyncTask<E: AbstractEntity<E>>(private val dao: AbstractDao<E>): AsyncTask<E, Void, Void>() {
    override fun doInBackground(vararg entities: E): Void? {
        dao.insert(*entities).forEachIndexed { index, id -> entities[index].id = id }
        return null
    }
}

class UpdateAsyncTask<E: AbstractEntity<E>>(private val dao: AbstractDao<E>): AsyncTask<E, Void, Void>() {
    override fun doInBackground(vararg entities: E): Void? {
        dao.update(*entities)
        return null
    }
}

class DeleteAsyncTask<E: AbstractEntity<E>>(private val dao: AbstractDao<E>): AsyncTask<E, Void, Void>() {
    override fun doInBackground(vararg entities: E): Void? {
        dao.delete(*entities)
        return null
    }
}

class DeleteAllAsyncTask<E: AbstractEntity<E>>(private val dao: AbstractDao<E>): AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg entities: Void): Void? {
        dao.deleteAll()
        return null
    }
}

class TagItemTask(private val itemTagJoinDao: ItemTagJoinDao, private val tagDao: TagDao, private val tagName: String): AsyncTask<CompositeNamedListEntity<Item, Tag>, Void, Void>() {
    override fun doInBackground(vararg itemsWithTags: CompositeNamedListEntity<Item, Tag>): Void? {
        val tag = tagDao.getByName(tagName) ?: Tag(tagName)

        if (tag.id == 0L) {
            tag.id = tagDao.insert(tag)
        }

        itemsWithTags.filter { !it.joinList.map { itemTagJoin -> itemTagJoin.tag!!.name }.contains(tagName) }
                .forEach { itemTagJoinDao.insert(ItemTagJoin(it.entity, tag)) }
        return null
    }
}

class RemoveTagTask(private val itemTagJoinDao: ItemTagJoinDao, private val tagDao: TagDao, private val tagName: String): AsyncTask<CompositeNamedListEntity<Item, Tag>, Void, Void>() {
    override fun doInBackground(vararg itemsWithTags: CompositeNamedListEntity<Item, Tag>): Void? {
        val tag = tagDao.getByName(tagName)
        itemsWithTags.filter { it.joinList.map { itemTagJoin -> itemTagJoin.tag!!.name }.contains(tagName) }
                .forEach { itemTagJoinDao.delete(itemTagJoinDao.getByItemAndTag(it.entity.id, tag!!.id)!!) }
        return null
    }
}

class ItemTagTask(private val itemTagJoinDao: ItemTagJoinDao, private val itemDao: ItemDao, private val itemName: String): AsyncTask<CompositeNamedListEntity<Tag, Item>, Void, Void>() {
    override fun doInBackground(vararg tagsWithItems: CompositeNamedListEntity<Tag, Item>): Void? {
        val item = itemDao.getByName(itemName) ?: Item(itemName)

        if (item.id == 0L) {
            item.id = itemDao.insert(item)
        }

        tagsWithItems.filter { !it.joinList.map { itemTagJoin -> itemTagJoin.item.name }.contains(itemName) }
                .forEach { itemTagJoinDao.insert(ItemTagJoin(item, it.entity)) }
        return null
    }
}

class RemoveItemTask(private val itemTagJoinDao: ItemTagJoinDao, private val itemDao: ItemDao, private val itemName: String): AsyncTask<CompositeNamedListEntity<Tag, Item>, Void, Void>() {
    override fun doInBackground(vararg tagsWithItems: CompositeNamedListEntity<Tag, Item>): Void? {
        val item = itemDao.getByName(itemName)
        tagsWithItems.filter { it.joinList.map { itemTagJoin -> itemTagJoin.item.name }.contains(itemName) }
                .forEach { itemTagJoinDao.delete(itemTagJoinDao.getByItemAndTag(item!!.id, it.entity.id)!!) }
        return null
    }
}
