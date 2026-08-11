// app/src/main/java/com/example/data/local/entity/PaymentPriorityEntity.kt
package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_priorities")
data class PaymentPriorityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val priorityNumber: Int, // 1 to 5
    val title: String, // "Alquiler", "Comida", "Internet", "Zapatos", "Deudas"
    val amountUsd: Double,
    val noteDetails: String = "",
    val isFuturePossible: Boolean = false, // true for "30-08-26 Posibles deudas"
    val isPaid: Boolean = false,
    val paidByHimPercent: Int = 50 // % covered by Him
)
