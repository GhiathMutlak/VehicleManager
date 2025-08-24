package com.carly.vehicles.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.carly.vehicles.data.database.entity.BrandEntity
import com.carly.vehicles.data.database.entity.SeriesEntity

@Dao
interface CatalogDao {
    @Query("SELECT * FROM catalog_brand")
    suspend fun brands(): List<BrandEntity>

    @Query("SELECT * FROM catalog_series WHERE brandId=:brandId")
    suspend fun series(brandId: String): List<SeriesEntity>

//    @Query("SELECT feature FROM catalog_features WHERE brandId=:brandId")
//    suspend fun features(brandId: String): List<FeatureEntity>
}