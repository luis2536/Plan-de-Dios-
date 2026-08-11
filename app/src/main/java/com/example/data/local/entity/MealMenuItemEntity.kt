// app/src/main/java/com/example/data/local/entity/MealMenuItemEntity.kt
package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_menu_items")
data class MealMenuItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayName: String, // Lunes, Martes, etc.
    val breakfast: String, // "Arepa o Pan"
    val lunch: String, // "Pollo, Arroz, Ensalada"
    val dinner: String // "Arepa o Pan"
)
