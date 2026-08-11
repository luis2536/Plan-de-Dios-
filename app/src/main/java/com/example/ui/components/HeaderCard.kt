// app/src/main/java/com/example/ui/components/HeaderCard.kt
package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.UserRole
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.LightGold

@Composable
fun HeaderCard(
    userRole: UserRole,
    language: AppLanguage,
    totalIncomeUsd: Double, // 260.0
    totalPrioritiesCostUsd: Double, // 160.0
    totalPantryCostUsd: Double, // 56.30
    modifier: Modifier = Modifier
) {
    val netSurplusUsd = (totalIncomeUsd - totalPrioritiesCostUsd).coerceAtLeast(0.0)
    val spentPercentage = if (totalIncomeUsd > 0) (totalPrioritiesCostUsd / totalIncomeUsd).toFloat() else 0f

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(DeepNavy, Color(0xFF1E293B), Color(0xFF0F172A))
                    )
                )
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Top Title Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.SPANISH) "Presupuesto Familiar" else "Household Budget",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "PLAN DE DIOS",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldAccent,
                            letterSpacing = 1.sp
                        )
                    }

                    Surface(
                        color = Color(0x33F59E0B),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = LightGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (language == AppLanguage.SPANISH) "Perfil: ${userRole.displayNameEs}" else "Profile: ${userRole.displayNameEn}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LightGold
                            )
                        }
                    }
                }

                Divider(color = Color(0x1AFFFFFF))

                // Income & Priorities Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.SPANISH) "Ingreso Mensual" else "Monthly Income",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "$${String.format("%.2f", totalIncomeUsd)} USD",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (language == AppLanguage.SPANISH) "Prioridades Pagos" else "Priority Payments",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "$${String.format("%.2f", totalPrioritiesCostUsd)} USD",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent
                        )
                    }
                }

                // Excedente Libre
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x1510B981), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (language == AppLanguage.SPANISH) "Disponible / Excedente:" else "Net Available Surplus:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "$${String.format("%.2f", netSurplusUsd)} USD",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldGreen
                    )
                }

                // 3D Budget Progress Bar
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (language == AppLanguage.SPANISH) "Compromiso de Sueldo:" else "Salary Allocation:",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "${(spentPercentage * 100).toInt()}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent
                        )
                    }

                    ProgressBar3D(
                        progress = spentPercentage,
                        barColorStart = GoldAccent,
                        barColorEnd = EmeraldGreen
                    )
                }
            }
        }
    }
}
