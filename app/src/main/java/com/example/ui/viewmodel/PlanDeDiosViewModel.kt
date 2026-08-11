// app/src/main/java/com/example/ui/viewmodel/PlanDeDiosViewModel.kt
package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.database.AppDatabase
import com.example.data.local.database.DatabaseInitializer
import com.example.data.local.entity.MealMenuItemEntity
import com.example.data.local.entity.PantryItemEntity
import com.example.data.local.entity.PaymentPriorityEntity
import com.example.data.local.entity.ProjectionEntity
import com.example.data.model.AppLanguage
import com.example.data.model.UserRole
import com.example.data.repository.FinanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatMessage(
    val sender: String, // "Usuario" or "Asistente AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class PlanDeDiosViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = FinanceRepository(db.planDeDiosDao())

    init {
        viewModelScope.launch {
            DatabaseInitializer.populateInitialDataIfEmpty(db.planDeDiosDao())
        }
    }

    // Role & Language State
    private val _currentRole = MutableStateFlow(UserRole.HOMBRE)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.SPANISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    // Room Flows
    val pantryItems: StateFlow<List<PantryItemEntity>> = repository.allPantryItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val paymentPriorities: StateFlow<List<PaymentPriorityEntity>> = repository.allPaymentPriorities.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val projections: StateFlow<List<ProjectionEntity>> = repository.allProjections.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val mealMenuItems: StateFlow<List<MealMenuItemEntity>> = repository.allMealMenuItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Pantry Operations
    fun addPantryItem(name: String, category: String, qty: String, priceUsd: Double, days: Int) {
        viewModelScope.launch {
            repository.addPantryItem(
                PantryItemEntity(
                    name = name,
                    category = category,
                    unitQuantity = qty,
                    priceUsd = priceUsd,
                    daysDuration = days
                )
            )
        }
    }

    fun togglePantryStocked(item: PantryItemEntity) {
        viewModelScope.launch {
            repository.updatePantryItem(item.copy(isStocked = !item.isStocked))
        }
    }

    fun deletePantryItem(id: Int) {
        viewModelScope.launch {
            repository.deletePantryItem(id)
        }
    }

    // Debt & Payment Operations
    fun addPaymentPriority(title: String, amountUsd: Double, priorityNum: Int, details: String, isFuture: Boolean) {
        viewModelScope.launch {
            repository.addPaymentPriority(
                PaymentPriorityEntity(
                    priorityNumber = priorityNum,
                    title = title,
                    amountUsd = amountUsd,
                    noteDetails = details,
                    isFuturePossible = isFuture
                )
            )
        }
    }

    fun togglePaymentPaid(priority: PaymentPriorityEntity) {
        viewModelScope.launch {
            repository.updatePaymentPriority(priority.copy(isPaid = !priority.isPaid))
        }
    }

    fun deletePaymentPriority(id: Int) {
        viewModelScope.launch {
            repository.deletePaymentPriority(id)
        }
    }

    // Projection Operations
    fun addProjection(title: String, cost: Double, cashPct: Int, creditPct: Int, himPct: Int, herPct: Int, months: Int) {
        viewModelScope.launch {
            repository.addProjection(
                ProjectionEntity(
                    title = title,
                    totalCostUsd = cost,
                    cashPercent = cashPct,
                    creditPercent = creditPct,
                    himSharePercent = himPct,
                    herSharePercent = herPct,
                    monthsInstallment = months
                )
            )
        }
    }

    fun deleteProjection(id: Int) {
        viewModelScope.launch {
            repository.deleteProjection(id)
        }
    }

    // AI Chat State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "Asistente AI",
                text = "¡Hola! Soy tu Asistente Financiero 'Plan de Dios'. He analizado tus ingresos ($260 Él) y tus $160 en prioridades fundamentales. ¿En qué te puedo asesorar hoy?"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    fun sendMessageToAi(userPrompt: String) {
        if (userPrompt.isBlank()) return
        val updated = _chatMessages.value + ChatMessage("Usuario", userPrompt)
        _chatMessages.value = updated
        _isAiLoading.value = true

        viewModelScope.launch {
            val totalPantryCost = pantryItems.value.sumOf { it.priceUsd }
            val totalPrioritiesCost = paymentPriorities.value.filter { !it.isFuturePossible }.sumOf { it.amountUsd }
            val contextSummary = """
                Ingreso mensual de Él: $260 USD.
                Total en prioridades fijas de pago: $$totalPrioritiesCost USD.
                Costo total de despensa actual: $$totalPantryCost USD.
                Proyecciones guardadas: ${projections.value.joinToString { "${it.title} ($${it.totalCostUsd})" }}
            """.trimIndent()

            val aiReply = repository.askGeminiAssistant(userPrompt, contextSummary)
            _isAiLoading.value = false
            _chatMessages.value = _chatMessages.value + ChatMessage("Asistente AI", aiReply)
        }
    }
}
