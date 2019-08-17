package org.omgcobra.matchthese.test

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.*
import org.junit.runner.RunWith
import org.omgcobra.matchthese.model.Item
import java.io.IOException
import java.lang.Exception

import org.hamcrest.core.Is.`is`
import org.junit.*
import org.junit.runners.MethodSorters
import org.omgcobra.matchthese.dao.*
import org.omgcobra.matchthese.model.ItemTagJoin
import org.omgcobra.matchthese.model.Tag

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.DEFAULT)
class DBReadWriteTest {
    private lateinit var itemDao: ItemDao
    private lateinit var tagDao: TagDao
    private lateinit var itemTagJoinDao: ItemTagJoinDao
    private lateinit var itemTagCompositeDao: ItemTagCompositeDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        db = AppDatabase.buildTest(ApplicationProvider.getApplicationContext())
        itemDao = db.itemDao()
        tagDao = db.tagDao()
        itemTagJoinDao = db.itemTagJoinDao()
        itemTagCompositeDao = db.itemTagCompositeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testCRUD() {
        val item = createItem("Tyler")
        val tag = createTag("cool")

        readItem(item)
        readTag(tag)

        joinItemAndTag(item, tag)

        checkJoin(item, tag)

        updateItem(item, "Stephanie")
        updateTag(tag, "awesome")

        checkJoin(item, tag)

        deleteTag(tag)
        assertThat(itemTagCompositeDao.loadItemWithTags(item.id).tagList as Collection<*>, `is`(empty()))
        deleteItem(item)
    }

    private fun joinItemAndTag(item: Item, tag: Tag) {
        val id = itemTagJoinDao.insert(ItemTagJoin(item, tag))
        assertThat(id, greaterThan(0L))
    }

    private fun checkJoin(item: Item, tag: Tag) {
        val itemWithTags = itemTagCompositeDao.loadItemWithTags(item.id)
        assertThat(itemWithTags.item, equalTo(item))
        assertThat(itemWithTags.tagList, contains(tag.name))
    }

    private fun createItem(name: String): Item {
        val item = Item(name)
        val itemId = itemDao.insert(item)
        assertThat(itemId, greaterThan(0L))
        item.id = itemId
        return item
    }

    private fun createTag(name: String): Tag {
        val tag = Tag(name)
        val tagId = tagDao.insert(tag)
        assertThat(tagId, greaterThan(0L))
        tag.id = tagId
        return tag
    }

    private fun readItem(item: Item) {
        val byId = itemDao.load(item.id)
        assertThat(byId, equalTo(item))

        val byName = itemDao.findItemsByName(item.name)[0]
        assertThat(byName, equalTo(item))
    }

    private fun readTag(tag: Tag) {
        val byId = tagDao.load(tag.id)
        assertThat(byId, equalTo(tag))

        val byName = tagDao.findTagsByName(tag.name)[0]
        assertThat(byName, equalTo(tag))
    }

    private fun updateItem(item: Item, name: String) {
        val oldName = item.name
        item.name = name
        itemDao.update(item)

        val byId = itemDao.load(item.id)
        assertThat(byId, equalTo(item))

        val byName = itemDao.findItemsByName(oldName)
        assertThat(byName as Collection<*>, `is`(empty()))
    }

    private fun updateTag(tag: Tag, name: String) {
        val oldName = tag.name
        tag.name = name
        tagDao.update(tag)

        val byId = tagDao.load(tag.id)
        assertThat(byId, equalTo(tag))

        val byName = tagDao.findTagsByName(oldName)
        assertThat(byName as Collection<*>, `is`(empty()))
    }

    private fun deleteItem(item: Item) {
        val id = item.id
        val name = item.name
        itemDao.delete(item)

        val byId = itemDao.load(id)
        assertThat(byId, nullValue())

        val byName = itemDao.findItemsByName(name)
        assertThat(byName as Collection<*>, `is`(empty()))

        assertThat(itemTagCompositeDao.loadItemWithTags(item.id), nullValue())
    }

    private fun deleteTag(tag: Tag) {
        val id = tag.id
        val name = tag.name
        tagDao.delete(tag)

        val byId = tagDao.load(id)
        assertThat(byId, nullValue())

        val byName = tagDao.findTagsByName(name)
        assertThat(byName as Collection<*>, `is`(empty()))
    }
}
