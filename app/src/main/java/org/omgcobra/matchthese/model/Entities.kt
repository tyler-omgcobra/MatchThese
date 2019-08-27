package org.omgcobra.matchthese.model

import androidx.room.*
import java.io.Serializable

abstract class AbstractEntity<T: AbstractEntity<T>>(@PrimaryKey(autoGenerate = true) var id: Long): Serializable, Comparable<T> {
    override fun compareTo(other: T) = this.id.compareTo(other.id)
}
abstract class CompositeListEntity<T: AbstractEntity<T>>(@Embedded val entity: T): Serializable, Comparable<CompositeListEntity<T>> {
    abstract var list: List<String>
    override fun compareTo(other: CompositeListEntity<T>) = this.entity.compareTo(other.entity)
}

@Entity(indices = [Index(value = ["id", "name"], unique = true)])
data class Item(var name: String): AbstractEntity<Item>(0L) {
    override fun compareTo(other: Item) = this.name.compareTo(other.name, true)
    override fun toString() = name
}

@Entity(indices = [Index(value = ["id", "name"], unique = true)])
data class Tag(var name: String): AbstractEntity<Tag>(0L) {
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

class ItemWithTags(entity: Item): CompositeListEntity<Item>(entity) {
    @Relation(
            parentColumn = "id",
            entityColumn = "itemid",
            entity = ItemTagJoin::class,
            projection = ["tagname"]
    ) override var list: List<String> = listOf()
}

class TagWithItems(entity: Tag): CompositeListEntity<Tag>(entity){
    @Relation(
            parentColumn = "id",
            entityColumn = "tagid",
            entity = ItemTagJoin::class,
            projection = ["itemname"]
    ) override var list: List<String> = listOf()
}
