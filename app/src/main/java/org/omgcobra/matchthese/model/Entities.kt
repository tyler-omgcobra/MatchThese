package org.omgcobra.matchthese.model

import androidx.room.*

abstract class AbstractEntity(@PrimaryKey(autoGenerate = true) var id: Long)

@Entity(indices = [Index(value = ["id", "name"], unique = true)])
data class Item(var name: String): AbstractEntity(0L), Comparable<Item> {
    override fun compareTo(other: Item) = this.name.compareTo(other.name, true)
}

@Entity(indices = [Index(value = ["id", "name"], unique = true)])
data class Tag(var name: String): AbstractEntity(0L), Comparable<Tag> {
    override fun compareTo(other: Tag) = this.name.compareTo(other.name, true)
}

@Entity(foreignKeys = [
            ForeignKey(entity = Tag::class,
                    parentColumns = ["id", "name"],
                    childColumns = ["tagid", "tagname"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE),
            ForeignKey(entity = Item::class,
                    parentColumns = ["id", "name"],
                    childColumns = ["itemid", "itemname"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE)
        ],
        indices = [
            Index(value = ["itemid", "itemname", "tagid", "tagname"], unique = true)
        ])
data class ItemTagJoin(
        @Embedded(prefix = "item") val item: Item,
        @Embedded(prefix = "tag") val tag: Tag?
): AbstractEntity(0L)

data class ItemWithTags(
        @Embedded val item: Item
): Comparable<ItemWithTags> {
    @Relation(
            parentColumn = "id",
            entityColumn = "itemid",
            entity = ItemTagJoin::class,
            projection = ["tagname"]
    ) var tagList: List<String> = listOf()

    override fun compareTo(other: ItemWithTags): Int {
        return this.item.compareTo(other.item)
    }
}

data class TagWithItems(
        @Embedded val tag: Tag
): Comparable<TagWithItems> {
    @Relation(
            parentColumn = "id",
            entityColumn = "tagid",
            entity = ItemTagJoin::class,
            projection = ["itemname"]
    ) var itemList: List<String> = listOf()

    override fun compareTo(other: TagWithItems): Int {
        return this.tag.compareTo(other.tag)
    }
}
