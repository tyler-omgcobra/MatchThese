package org.omgcobra.matchthese.model

import androidx.room.*

abstract class AbstractEntity(@PrimaryKey(autoGenerate = true) var id: Long)

@Entity(indices = [Index(value = ["id", "name"], unique = true)])
data class Item(var name: String): AbstractEntity(0L)

@Entity(indices = [Index(value = ["id", "name"], unique = true)])
data class Tag(var name: String): AbstractEntity(0L)

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
        ])
data class ItemTagJoin(
        @Embedded(prefix = "item") val item: Item,
        @Embedded(prefix = "tag") val tag: Tag?
): AbstractEntity(0L)

data class ItemWithTags(
        @Embedded val item: Item
) {
    @Relation(
            parentColumn = "id",
            entityColumn = "itemid",
            entity = ItemTagJoin::class,
            projection = ["tagname"]
    ) var tagList: List<String> = listOf()
}
