package org.omgcobra.matchthese.model

import androidx.room.*


@Entity data class Item(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val name: String
)

@Entity data class Tag(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val name: String
)

@Entity(primaryKeys = ["tagid", "itemid"],
        foreignKeys = [
            ForeignKey(entity = Tag::class,
                    parentColumns = ["id"],
                    childColumns = ["tagid"]),
            ForeignKey(entity = Item::class,
                    parentColumns = ["id"],
                    childColumns = ["itemid"])
        ]/*,
        indices = [Index(unique = true, value = ["itemid", "tagid"])]*/)
data class ItemTagJoin(
        @Embedded(prefix = "tag") val tag: Tag,
        @Embedded(prefix = "item") val item: Item
)

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
