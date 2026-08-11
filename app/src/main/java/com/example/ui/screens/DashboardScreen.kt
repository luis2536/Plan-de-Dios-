// app/src/main/java/com/example/ui/screens/DashboardScreen.kt
package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.MealMenuItemEntity
import com.example.data.local.entity.PantryItemEntity
import com.example.data.local.entity.PaymentPriorityEntity
import com.example.data.model.AppLanguage
import com.example.data.model.UserRole
import com.example.ui.components.*
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.viewmodel.PlanDeDiosViewModel

@Composable
fun DashboardScreen(
    viewModel: PlanDeDiosViewModel,
    modifier: Modifier = Modifier
) {
    val currentRole by viewModel.currentRole.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val pantryItems by viewModel.pantryItems.collectAsState()
    val priorities by viewModel.paymentPriorities.collectAsState()
    val mealMenu by viewModel.mealMenuItems.collectAsState()

    val totalIncome = currentRole.defaultIncome // $260
    val totalPrioritiesCost = priorities.filter { !it.isFuturePossible }.sumOf { it.amountUsd } // $160
    val totalPantryCost = pantryItems.sumOf { it.priceUsd } // $56.30

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Language & Role Bar
        item {
            LanguageRoleBar(
                currentRole = currentRole,
                currentLanguage = currentLanguage,
                onRoleSelected = { viewModel.setRole(it) },
                onLanguageSelected = { viewModel.setLanguage(it) }
            )
        }

        // 2. Main Budget Header Card
        item {
            HeaderCard(
                userRole = currentRole,
                language = currentLanguage,
                totalIncomeUsd = totalIncome,
                totalPrioritiesCostUsd = totalPrioritiesCost,
                totalPantryCostUsd = totalPantryCost
            )
        }

        // 3. Stat Cards Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = if (currentLanguage == AppLanguage.SPANISH) "Presupuesto Despensa" else "Pantry Budget",
                    value = "$${String.format("%.2f", totalPantryCost)} USD",
                    subtitle = if (currentLanguage == AppLanguage.SPANISH) "22 productos surtidos" else "22 items stocked",
                    icon = Icons.Default.ShoppingBag,
                    iconColor = GoldAccent,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = if (currentLanguage == AppLanguage.SPANISH) "Prioridades Pago" else "Priority Debt",
                    value = "$${String.format("%.2f", totalPrioritiesCost)} USD",
                    subtitle = if (currentLanguage == AppLanguage.SPANISH) "5 rubros fijados" else "5 fixed items",
                    icon = Icons.Default.Payments,
                    iconColor = EmeraldGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 4. Custom Expenses Bar Chart
        item {
            val chartItems = listOf(
                BarItem("Alquiler", 43f, GoldAccent),
                BarItem("Comida", 40f, EmeraldGreen),
                BarItem("Internet", 20f, Color(0xFF3B82F6)),
                BarItem("Zapatos", 30f, Color(0xFFA855F7)),
                BarItem("Deudas", 25f, Color(0xFFEF4444))
            )
            CustomBarChart(
                title = if (currentLanguage == AppLanguage.SPANISH) "Distribución de Prioridades ($160 USD)" else "Priority Breakdown ($160 USD)",
                barItems = chartItems
            )
        }

        // 5. Weekly Meal Menu Card (From Image 3)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestaurantMenu,
                            contentDescription = null,
                            tint = GoldAccent
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.SPANISH) "Menú Semanal Organizado" else "Weekly Planned Menu",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    mealMenu.take(4).forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = item.dayName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = GoldAccent
                                )
                                Text(
                                    text = "Almuerzo: ${item.lunch}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = item.breakfast,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
