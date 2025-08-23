package com.carly.vehicles.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carly.vehicles.data.database.entity.FeatureEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FeatureDao {
    @Query("SELECT * FROM features WHERE brandId = :brandId ORDER BY name ASC")
    fun observeByBrand(brandId: String): Flow<List<FeatureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(features: List<FeatureEntity>)

    @Query("DELETE FROM features")
    suspend fun clearAll()
}