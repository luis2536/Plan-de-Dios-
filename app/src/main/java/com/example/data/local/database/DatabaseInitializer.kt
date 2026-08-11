// app/src/main/java/com/example/data/local/database/DatabaseInitializer.kt
package com.example.data.local.database

import com.example.data.local.dao.PlanDeDiosDao
import com.example.data.local.entity.MealMenuItemEntity
import com.example.data.local.entity.PantryItemEntity
import com.example.data.local.entity.PaymentPriorityEntity
import com.example.data.local.entity.ProjectionEntity
import kotlinx.coroutines.flow.first

object DatabaseInitializer {

    suspend fun populateInitialDataIfEmpty(dao: PlanDeDiosDao) {
        val existingItems = dao.getAllPantryItems().first()
        if (existingItems.isNotEmpty()) return // Already populated

        // 1. Seed Pantry Items
        val pantryList = listOf(
            // Desayuno / Cena
            PantryItemEntity(name = "Harina Pan", category = "Desayuno/Cena", unitQuantity = "3 und", priceUsd = 3.00, daysDuration = 7),
            PantryItemEntity(name = "Harina Trigo", category = "Desayuno/Cena", unitQuantity = "2 und", priceUsd = 2.50, daysDuration = 4),
            PantryItemEntity(name = "Pan Sánduche", category = "Desayuno/Cena", unitQuantity = "2 und", priceUsd = 2.30, daysDuration = 4),
            PantryItemEntity(name = "Salchicha", category = "Desayuno/Cena", unitQuantity = "1/2 und", priceUsd = 2.20, daysDuration = 5),
            PantryItemEntity(name = "Queso", category = "Desayuno/Cena", unitQuantity = "1 kg", priceUsd = 5.50, daysDuration = 15),
            PantryItemEntity(name = "Huevos", category = "Desayuno/Cena", unitQuantity = "1/2 cartón", priceUsd = 6.00, daysDuration = 15),
            PantryItemEntity(name = "Sardina", category = "Desayuno/Cena", unitQuantity = "5 und", priceUsd = 5.00, daysDuration = 5),

            // Almuerzo
            PantryItemEntity(name = "Arroz", category = "Almuerzo", unitQuantity = "3 und", priceUsd = 3.30, daysDuration = 9),
            PantryItemEntity(name = "Pasta", category = "Almuerzo", unitQuantity = "2 und", priceUsd = 3.70, daysDuration = 5),
            PantryItemEntity(name = "Pollo Entero", category = "Almuerzo", unitQuantity = "1.5 kg", priceUsd = 5.00, daysDuration = 15),
            PantryItemEntity(name = "Lenteja", category = "Almuerzo", unitQuantity = "1 kg", priceUsd = 2.30, daysDuration = 3),
            PantryItemEntity(name = "Aceite", category = "Almuerzo", unitQuantity = "1.5 LT", priceUsd = 3.50, daysDuration = 15),
            PantryItemEntity(name = "Mantequilla", category = "Almuerzo", unitQuantity = "1/2 und", priceUsd = 3.70, daysDuration = 15),
            PantryItemEntity(name = "Café", category = "Almuerzo", unitQuantity = "400 gr", priceUsd = 2.30, daysDuration = 15),
            PantryItemEntity(name = "Azúcar", category = "Almuerzo", unitQuantity = "1 kg", priceUsd = 1.50, daysDuration = 15),
            PantryItemEntity(name = "Sal", category = "Almuerzo", unitQuantity = "1 kg", priceUsd = 0.80, daysDuration = 15),

            // Aliños y Condimentos
            PantryItemEntity(name = "Salsa de Tomate", category = "Aliños y Condimentos", unitQuantity = "1 und", priceUsd = 2.30, daysDuration = 15),
            PantryItemEntity(name = "Mayonesa", category = "Aliños y Condimentos", unitQuantity = "1 und", priceUsd = 2.70, daysDuration = 15),
            PantryItemEntity(name = "Cebollas", category = "Aliños y Condimentos", unitQuantity = "7 und", priceUsd = 2.00, daysDuration = 15),
            PantryItemEntity(name = "Ajos", category = "Aliños y Condimentos", unitQuantity = "3 und", priceUsd = 1.30, daysDuration = 15),
            PantryItemEntity(name = "Papa", category = "Aliños y Condimentos", unitQuantity = "7 und", priceUsd = 2.50, daysDuration = 15),
            PantryItemEntity(name = "Zanahoria", category = "Aliños y Condimentos", unitQuantity = "4 und", priceUsd = 1.70, daysDuration = 15)
        )
        dao.insertAllPantryItems(pantryList)

        // 2. Seed Payment Priorities (Image 1 notes)
        val prioritiesList = listOf(
            PaymentPriorityEntity(priorityNumber = 1, title = "Alquiler", amountUsd = 43.00, noteDetails = "Pago mensual prioritario de vivienda", isFuturePossible = false, paidByHimPercent = 100),
            PaymentPriorityEntity(priorityNumber = 2, title = "Comida", amountUsd = 40.00, noteDetails = "+ $30 Cashea = Total $70 en cuota alimentación", isFuturePossible = false, paidByHimPercent = 60),
            PaymentPriorityEntity(priorityNumber = 3, title = "Internet", amountUsd = 20.00, noteDetails = "Servicio de conectividad hogar", isFuturePossible = false, paidByHimPercent = 50),
            PaymentPriorityEntity(priorityNumber = 4, title = "Zapatos", amountUsd = 30.00, noteDetails = "Calzado fundamental de uso personal", isFuturePossible = false, paidByHimPercent = 50),
            PaymentPriorityEntity(priorityNumber = 5, title = "Deudas Varias", amountUsd = 25.00, noteDetails = "Abuela ($5), Ali Huevo ($5), Corte Panal ($10), Torta ($4), Carmelina ($6.20)", isFuturePossible = false, paidByHimPercent = 100),

            // Posibles deudas al 30-08-26
            PaymentPriorityEntity(priorityNumber = 6, title = "Zapatos Quincena 2", amountUsd = 30.00, noteDetails = "Proyección gasto quincenal", isFuturePossible = true, paidByHimPercent = 50),
            PaymentPriorityEntity(priorityNumber = 7, title = "Deuda María", amountUsd = 25.00, noteDetails = "Pendiente de pago 30-08-26", isFuturePossible = true, paidByHimPercent = 100),
            PaymentPriorityEntity(priorityNumber = 8, title = "Cuota Cashea", amountUsd = 30.00, noteDetails = "Compromiso de financiamiento", isFuturePossible = true, paidByHimPercent = 40),
            PaymentPriorityEntity(priorityNumber = 9, title = "Comida Reserva", amountUsd = 40.00, noteDetails = "Reserva mercado fin de mes", isFuturePossible = true, paidByHimPercent = 60),
            PaymentPriorityEntity(priorityNumber = 10, title = "Varios (Recargos, Pasaje, Hielos, Papeles)", amountUsd = 15.00, noteDetails = "Gastos menores operativos", isFuturePossible = true, paidByHimPercent = 100)
        )
        dao.insertAllPaymentPriorities(prioritiesList)

        // 3. Seed Projections (User's specific example!)
        val projection = ProjectionEntity(
            title = "Aire Acondicionado 5,000 BTU",
            totalCostUsd = 50.00,
            cashPercent = 60, // $30 Contado
            creditPercent = 40, // $20 Crédito
            himSharePercent = 60, // $30 Él (Contado)
            herSharePercent = 40, // $20 Ella (Crédito)
            monthsInstallment = 4,
            status = "Planificado"
        )
        dao.insertProjection(projection)

        // 4. Seed Meal Menu (Image 3)
        val menuList = listOf(
            MealMenuItemEntity(dayName = "Lunes", breakfast = "Arepa o Pan con Queso/Embutidos", lunch = "Pollo Frito, Arroz, Ensalada", dinner = "Arepa o Pan"),
            MealMenuItemEntity(dayName = "Martes", breakfast = "Arepa o Pan", lunch = "Lenteja y Arroz", dinner = "Arepa o Pan con Queso"),
            MealMenuItemEntity(dayName = "Miércoles", breakfast = "Arepa o Pan", lunch = "Pasta con Salchicha", dinner = "Pan con Queso"),
            MealMenuItemEntity(dayName = "Jueves", breakfast = "Arepa o Pan", lunch = "Sardina y Arroz", dinner = "Arepa o Pan"),
            MealMenuItemEntity(dayName = "Viernes", breakfast = "Arepa o Pan", lunch = "Pollo en Salsa", dinner = "Arepa y Queso"),
            MealMenuItemEntity(dayName = "Sábado", breakfast = "Arepa o Pan", lunch = "Pollo y Pasta o Salchicha", dinner = "Arepa y Huevo"),
            MealMenuItemEntity(dayName = "Domingo", breakfast = "Ponqueca o Pan", lunch = "Pasta y Pollo", dinner = "Arepa y Queso")
        )
        dao.insertAllMealMenuItems(menuList)
    }
}
