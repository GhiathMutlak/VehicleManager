package com.carly.vehicles.data.datasource.catalog

import com.carly.vehicles.data.database.dao.BrandDao
import com.carly.vehicles.data.database.dao.FeatureDao
import com.carly.vehicles.data.database.dao.SeriesDao
import com.carly.vehicles.data.database.entity.BrandEntity
import com.carly.vehicles.data.database.entity.FeatureEntity
import com.carly.vehicles.data.database.entity.SeriesEntity
import com.carly.vehicles.data.repo.datastore.SeedFlagRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogSeeder @Inject constructor(
    private val catalogLoader: CatalogLoader,
    private val brandDao: BrandDao,
    private val seriesDao: SeriesDao,
    private val featureDao: FeatureDao,
    private val seedFlagRepo: SeedFlagRepo
) {

    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        if (seedFlagRepo.isCatalogSeeded()) return@withContext

        // 1. Read JSON from assets
        val catalog = catalogLoader.loadCatalog()

        // 3. Convert DTOs to entities
        val brandEntities = catalog.map {
            BrandEntity(it.brandId, it.brandName)
        }

        val seriesEntities = catalog.flatMap { brand ->
            brand.series.map { series ->
                SeriesEntity(
                    id = series.id,
                    brandId = brand.brandId,
                    name = series.name,
                    minYear = series.minYear,
                    maxYear = series.maxYear
                )
            }
        }

        val featureEntities = catalog.flatMap { brand ->
            brand.features.map { feature ->
                FeatureEntity(
                    id = "${brand.brandId}_$feature",
                    brandId = brand.brandId,
                    name = feature
                )
            }
        }

        // 4. Insert into Room
        brandDao.insertAll(brandEntities)
        seriesDao.insertAll(seriesEntities)
        featureDao.insertAll(featureEntities)

        seedFlagRepo.setCatalogSeeded()
    }
}