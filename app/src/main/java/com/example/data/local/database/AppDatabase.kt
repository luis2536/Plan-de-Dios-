// app/src/main/java/com/example/data/local/database/AppDatabase.kt
package com.example.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.PlanDeDiosDao
import com.example.data.local.entity.MealMenuItemEntity
import com.example.data.local.entity.PantryItemEntity
import com.example.data.local.entity.PaymentPriorityEntity
import com.example.data.local.entity.ProjectionEntity

@Database(
    entities = [
        PantryItemEntity::class,
        PaymentPriorityEntity::class,
        ProjectionEntity::class,
        MealMenuItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun planDeDiosDao(): PlanDeDiosDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "plan_de_dios_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
