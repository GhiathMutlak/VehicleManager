package com.carly.vehicles.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.carly.vehicles.data.database.dao.BrandDao
import com.carly.vehicles.data.database.dao.FeatureDao
import com.carly.vehicles.data.database.dao.SeriesDao
import com.carly.vehicles.data.database.dao.VehicleDao
import com.carly.vehicles.data.datasource.catalog.CatalogLoader
import com.carly.vehicles.data.datasource.catalog.CatalogSeeder
import com.carly.vehicles.data.repo.RoomCatalogRepo
import com.carly.vehicles.data.repo.RoomVehicleRepo
import com.carly.vehicles.data.repo.datastore.DataStoreSelectionRepo
import com.carly.vehicles.data.repo.datastore.SeedFlagRepo
import com.carly.vehicles.domain.repo.CatalogRepo
import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.repo.VehicleSelectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCatalogSeeder(
        catalogLoader: CatalogLoader,
        brandDao: BrandDao,
        seriesDao: SeriesDao,
        featureDao: FeatureDao,
        seedFlagRepo: SeedFlagRepo
    ): CatalogSeeder {
        return CatalogSeeder(catalogLoader, brandDao, seriesDao, featureDao, seedFlagRepo)
    }

    @Provides
    @Singleton
    fun provideSelectionRepo(dataStore: DataStore<Preferences>): VehicleSelectionRepository =
        DataStoreSelectionRepo(dataStore)

    @Provides
    @Singleton
    fun provideSeedFlagRepo(dataStore: DataStore<Preferences>): SeedFlagRepo =
        SeedFlagRepo(dataStore)

    @Provides
    @Singleton
    fun provideVehicleRepo(vehicleDao: VehicleDao): VehicleRepo =
        RoomVehicleRepo(vehicleDao)

    @Provides
    @Singleton
    fun provideCatalogRepo(
        brandDao: BrandDao,
        seriesDao: SeriesDao,
        featureDao: FeatureDao
    ): CatalogRepo = RoomCatalogRepo(brandDao, seriesDao, featureDao)
}