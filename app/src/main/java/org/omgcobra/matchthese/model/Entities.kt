package org.omgcobra.matchthese.model

import androidx.room.*
import java.io.Serializable

abstract class AbstractEntity<T: AbstractEntity<T>>(@PrimaryKey(autoGenerate = true) var id: Long): Serializable
abstract class NamedEntity<T: NamedEntity<T>>: AbstractEntity<T>(0L), Comparable<T> {
    abstract var name: String
    override fun compareTo(other: T) = this.name.compareTo(other.name, true)
}
abstract class CompositeNamedListEntity<T: NamedEntity<T>, L: AbstractEntity<L>>(@Embedded val entity: T): Serializable, Comparable<CompositeNamedListEntity<T, L>> {
    abstract var joinList: List<ItemTagJoin>
    override fun compareTo(other: CompositeNamedListEntity<T, L>) = this.entity.compareTo(other.entity)
    override fun toString() = this.entity.toString()
}

@Entity(indices = [Index(value = ["id", "name"], unique = true)])
data class Item(override var name: String): NamedEntity<Item>() {
    override fun compareTo(other: Item) = this.name.compareTo(other.name, true)
    override fun toString() = name
}

@Entity(indices = [Index(value = ["id", "name"], unique = true)])
data class Tag(override var name: String): NamedEntity<Tag>() {
    override fun compareTo(other: Tag) = this.name.compareTo(other.name, true)
    override fun toString() = name
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
): AbstractEntity<ItemTagJoin>(0L)

class ItemWithTags(entity: Item): CompositeNamedListEntity<Item, Tag>(entity) {
    @Relation(
            parentColumn = "id",
            entityColumn = "itemid",
            entity = ItemTagJoin::class
    ) override var joinList: List<ItemTagJoin> = listOf()
}

class TagWithItems(entity: Tag): CompositeNamedListEntity<Tag, Item>(entity){
    @Relation(
            parentColumn = "id",
            entityColumn = "tagid",
            entity = ItemTagJoin::class
    ) override var joinList: List<ItemTagJoin> = listOf()

    override fun compareTo(other: CompositeNamedListEntity<Tag, Item>) =
            when (val compare = -this.joinList.size.compareTo(other.joinList.size)) {
                0 -> super.compareTo(other)
                else -> compare
            }
    override fun toString() = "%s (%d)".format(this.entity.name, this.joinList.size)
}
