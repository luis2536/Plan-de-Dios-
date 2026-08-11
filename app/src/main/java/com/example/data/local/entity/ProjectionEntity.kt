// app/src/main/java/com/example/data/local/entity/ProjectionEntity.kt
package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchase_projections")
data class ProjectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String, // "Aire Acondicionado 5,000 BTU"
    val totalCostUsd: Double, // 50.0
    val cashPercent: Int = 60, // 60% contado
    val creditPercent: Int = 40, // 40% crédito
    val himSharePercent: Int = 60, // Him pays 60%
    val herSharePercent: Int = 40, // Her pays 40%
    val monthsInstallment: Int = 4, // months credit
    val status: String = "Planificado" // "Planificado", "Aprobado", "En Pago"
)
