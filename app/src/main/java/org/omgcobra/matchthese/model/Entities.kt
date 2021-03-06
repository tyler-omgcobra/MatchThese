package org.omgcobra.matchthese.model

import androidx.room.*
import java.io.Serializable
import java.math.BigDecimal

abstract class AbstractEntity<T: AbstractEntity<T>>(@PrimaryKey(autoGenerate = true) var id: Long): Serializable
abstract class NamedEntity<T: NamedEntity<T>>: AbstractEntity<T>(0L), Comparable<T> {
    abstract var name: String
    override fun compareTo(other: T) = this.name.compareTo(other.name, true)
    companion object {
        inline fun <reified T: NamedEntity<T>> create(name: String): T {
            return (when (T::class) {
                Recipe::class -> Recipe(name)
                Ingredient::class -> Ingredient(name)
                else -> throw IllegalArgumentException()
            }) as T
        }
    }
}
abstract class CompositeNamedListEntity<T: NamedEntity<T>, L: AbstractEntity<L>>(@Embedded val entity: T): Serializable, Comparable<CompositeNamedListEntity<T, L>> {
    abstract var joinList: List<RecipeIngredientJoin>
    override fun compareTo(other: CompositeNamedListEntity<T, L>) = this.entity.compareTo(other.entity)
    override fun toString() = this.entity.toString()
}

@Entity(indices = [Index(value = ["id", "name"], unique = true)])
data class Recipe(override var name: String): NamedEntity<Recipe>() {
    var onShoppingList = false
    override fun compareTo(other: Recipe) = this.name.compareTo(other.name, true)
    override fun toString() = name
}

@Entity(indices = [Index(value = ["id", "name"], unique = true)])
data class Ingredient(override var name: String): NamedEntity<Ingredient>() {
    var inPantry = false
    var pantryAmount: Double? = null
    var pantryUOM: String? = null
    override fun compareTo(other: Ingredient) = this.name.compareTo(other.name, true)
    override fun toString() = name
}

@Entity(foreignKeys = [
            ForeignKey(entity = Ingredient::class,
                    parentColumns = ["id", "name"],
                    childColumns = ["ingredientid", "ingredientname"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE),
            ForeignKey(entity = Recipe::class,
                    parentColumns = ["id", "name"],
                    childColumns = ["recipeid", "recipename"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE)
        ],
        indices = [
            Index(value = ["recipeid", "recipename", "ingredientid", "ingredientname"], unique = true)
        ])
data class RecipeIngredientJoin(
        @Embedded(prefix = "recipe") var recipe: Recipe,
        @Embedded(prefix = "ingredient") var ingredient: Ingredient?,
        var amount: BigDecimal,
        var unit: String
): AbstractEntity<RecipeIngredientJoin>(0L)

class RecipeWithIngredients(entity: Recipe): CompositeNamedListEntity<Recipe, Ingredient>(entity) {
    @Relation(
            parentColumn = "id",
            entityColumn = "recipeid",
            entity = RecipeIngredientJoin::class
    ) override var joinList: List<RecipeIngredientJoin> = listOf()
}

class IngredientWithRecipes(entity: Ingredient): CompositeNamedListEntity<Ingredient, Recipe>(entity){
    @Relation(
            parentColumn = "id",
            entityColumn = "ingredientid",
            entity = RecipeIngredientJoin::class
    ) override var joinList: List<RecipeIngredientJoin> = listOf()

    override fun compareTo(other: CompositeNamedListEntity<Ingredient, Recipe>) =
            when (val compare = -this.joinList.size.compareTo(other.joinList.size)) {
                0 -> super.compareTo(other)
                else -> compare
            }
    override fun toString() = "%s (%d)".format(this.entity.name, this.joinList.size)
}
