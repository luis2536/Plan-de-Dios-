// app/src/main/java/com/example/data/repository/FinanceRepository.kt
package com.example.data.repository

import com.example.data.local.dao.PlanDeDiosDao
import com.example.data.local.entity.MealMenuItemEntity
import com.example.data.local.entity.PantryItemEntity
import com.example.data.local.entity.PaymentPriorityEntity
import com.example.data.local.entity.ProjectionEntity
import com.example.data.remote.GeminiApiService
import kotlinx.coroutines.flow.Flow

class FinanceRepository(private val dao: PlanDeDiosDao) {

    // Pantry Flow
    val allPantryItems: Flow<List<PantryItemEntity>> = dao.getAllPantryItems()

    suspend fun addPantryItem(item: PantryItemEntity) = dao.insertPantryItem(item)
    suspend fun updatePantryItem(item: PantryItemEntity) = dao.updatePantryItem(item)
    suspend fun deletePantryItem(id: Int) = dao.deletePantryItemById(id)

    // Payment Priorities Flow
    val allPaymentPriorities: Flow<List<PaymentPriorityEntity>> = dao.getAllPaymentPriorities()

    suspend fun addPaymentPriority(priority: PaymentPriorityEntity) = dao.insertPaymentPriority(priority)
    suspend fun updatePaymentPriority(priority: PaymentPriorityEntity) = dao.updatePaymentPriority(priority)
    suspend fun deletePaymentPriority(id: Int) = dao.deletePaymentPriorityById(id)

    // Projections Flow
    val allProjections: Flow<List<ProjectionEntity>> = dao.getAllProjections()

    suspend fun addProjection(projection: ProjectionEntity) = dao.insertProjection(projection)
    suspend fun updateProjection(projection: ProjectionEntity) = dao.updateProjection(projection)
    suspend fun deleteProjection(id: Int) = dao.deleteProjectionById(id)

    // Meal Menu Flow
    val allMealMenuItems: Flow<List<MealMenuItemEntity>> = dao.getAllMealMenu()

    // Gemini AI Consultation
    suspend fun askGeminiAssistant(prompt: String, contextSummary: String): String {
        return GeminiApiService.queryFinancialAssistant(prompt, contextSummary)
    }
}
