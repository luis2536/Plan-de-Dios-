// app/src/main/java/com/example/data/local/dao/PlanDeDiosDao.kt
package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.MealMenuItemEntity
import com.example.data.local.entity.PantryItemEntity
import com.example.data.local.entity.PaymentPriorityEntity
import com.example.data.local.entity.ProjectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDeDiosDao {

    // Pantry Items
    @Query("SELECT * FROM pantry_items ORDER BY category ASC, name ASC")
    fun getAllPantryItems(): Flow<List<PantryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPantryItem(item: PantryItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPantryItems(items: List<PantryItemEntity>)

    @Update
    suspend fun updatePantryItem(item: PantryItemEntity)

    @Query("DELETE FROM pantry_items WHERE id = :id")
    suspend fun deletePantryItemById(id: Int)

    // Payment Priorities
    @Query("SELECT * FROM payment_priorities ORDER BY priorityNumber ASC")
    fun getAllPaymentPriorities(): Flow<List<PaymentPriorityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentPriority(priority: PaymentPriorityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPaymentPriorities(priorities: List<PaymentPriorityEntity>)

    @Update
    suspend fun updatePaymentPriority(priority: PaymentPriorityEntity)

    @Query("DELETE FROM payment_priorities WHERE id = :id")
    suspend fun deletePaymentPriorityById(id: Int)

    // Projections
    @Query("SELECT * FROM purchase_projections ORDER BY id DESC")
    fun getAllProjections(): Flow<List<ProjectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjection(projection: ProjectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProjections(projections: List<ProjectionEntity>)

    @Update
    suspend fun updateProjection(projection: ProjectionEntity)

    @Query("DELETE FROM purchase_projections WHERE id = :id")
    suspend fun deleteProjectionById(id: Int)

    // Meal Menu
    @Query("SELECT * FROM meal_menu_items ORDER BY id ASC")
    fun getAllMealMenu(): Flow<List<MealMenuItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMealMenuItems(menuItems: List<MealMenuItemEntity>)
}
