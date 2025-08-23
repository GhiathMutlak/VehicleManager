package com.carly.vehicles.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.carly.vehicles.data.database.dao.BrandDao
import com.carly.vehicles.data.database.dao.FeatureDao
import com.carly.vehicles.data.database.dao.SeriesDao
import com.carly.vehicles.data.datasource.catalog.CatalogLoader
import com.carly.vehicles.data.datasource.catalog.CatalogSeeder
import com.carly.vehicles.data.datastore.DataStoreSelectionRepo
import com.carly.vehicles.data.datastore.SeedFlagRepo
import com.carly.vehicles.domain.repo.SelectionRepo
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
    fun provideSelectionRepo(dataStore: DataStore<Preferences>): SelectionRepo =
        DataStoreSelectionRepo(dataStore)

    @Provides
    @Singleton
    fun provideSeedFlagRepo(dataStore: DataStore<Preferences>): SeedFlagRepo =
        SeedFlagRepo(dataStore)
}