// app/src/main/java/com/example/data/local/entity/PantryItemEntity.kt
package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pantry_items")
data class PantryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // "Desayuno/Cena", "Almuerzo", "Aliños y Condimentos"
    val unitQuantity: String, // e.g. "3 und", "1.5 kg"
    val priceUsd: Double,
    val daysDuration: Int, // e.g. 7 días, 15 días
    val dateAdded: Long = System.currentTimeMillis(),
    val isStocked: Boolean = true
) {
    val costPerDay: Double
        get() = if (daysDuration > 0) priceUsd / daysDuration else 0.0
}
