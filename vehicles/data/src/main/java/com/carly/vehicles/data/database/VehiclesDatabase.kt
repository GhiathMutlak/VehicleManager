package com.carly.vehicles.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carly.vehicles.data.database.dao.BrandDao
import com.carly.vehicles.data.database.dao.CatalogDao
import com.carly.vehicles.data.database.dao.FeatureDao
import com.carly.vehicles.data.database.dao.SeriesDao
import com.carly.vehicles.data.database.dao.VehicleDao
import com.carly.vehicles.data.database.entity.BrandEntity
import com.carly.vehicles.data.database.entity.BrandFeatureEntity
import com.carly.vehicles.data.database.entity.FeatureEntity
import com.carly.vehicles.data.database.entity.SeriesEntity
import com.carly.vehicles.data.database.entity.VehicleEntity

@Database(
    entities = [
        VehicleEntity::class,
        BrandEntity::class,
        SeriesEntity::class,
        BrandFeatureEntity::class,
        FeatureEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VehiclesDatabase : RoomDatabase() {
    abstract fun brandDao(): BrandDao
    abstract fun catalogDao(): CatalogDao
    abstract fun featureDao(): FeatureDao
    abstract fun seriesDao(): SeriesDao
    abstract fun vehicleDao(): VehicleDao
}