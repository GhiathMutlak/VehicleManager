package com.carly.vehicles.data.di

import android.content.Context
import androidx.room.Room
import com.carly.vehicles.data.database.VehiclesDatabase
import com.carly.vehicles.data.database.dao.BrandDao
import com.carly.vehicles.data.database.dao.FeatureDao
import com.carly.vehicles.data.database.dao.SeriesDao
import com.carly.vehicles.data.database.dao.VehicleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VehiclesDatabase {
        return Room.databaseBuilder(
            context,
            VehiclesDatabase::class.java,
            "vehicles_db"
        ).build()
    }

    @Provides
    fun provideBrandDao(db: VehiclesDatabase): BrandDao = db.brandDao()

    @Provides
    fun provideSeriesDao(db: VehiclesDatabase): SeriesDao = db.seriesDao()

    @Provides
    fun provideFeatureDao(db: VehiclesDatabase): FeatureDao = db.featureDao()

    @Provides
    fun provideVehicleDao(db: VehiclesDatabase): VehicleDao = db.vehicleDao()
}
