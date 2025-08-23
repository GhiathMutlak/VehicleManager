package com.carly.vehicles.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carly.vehicles.data.database.entity.BrandEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandDao {
    @Query("SELECT * FROM catalog_brand ORDER BY name ASC")
    fun observeAll(): Flow<List<BrandEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(brands: List<BrandEntity>)

    @Query("DELETE FROM catalog_brand")
    suspend fun clearAll()
}