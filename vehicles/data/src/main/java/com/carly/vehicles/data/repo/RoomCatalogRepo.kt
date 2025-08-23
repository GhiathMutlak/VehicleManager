package com.carly.vehicles.data.repo

import com.carly.vehicles.data.database.dao.BrandDao
import com.carly.vehicles.data.database.dao.FeatureDao
import com.carly.vehicles.data.database.dao.SeriesDao
import com.carly.vehicles.data.mapper.toBrand
import com.carly.vehicles.data.mapper.toFeature
import com.carly.vehicles.data.mapper.toSeries
import com.carly.vehicles.domain.model.Brand
import com.carly.vehicles.domain.model.Feature
import com.carly.vehicles.domain.model.Series
import com.carly.vehicles.domain.repo.CatalogRepo
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.Result
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RoomCatalogRepo @Inject constructor(
    private val brandDao: BrandDao,
    private val seriesDao: SeriesDao,
    private val featureDao: FeatureDao
) : CatalogRepo {

    override suspend fun getBrands(): Result<List<Brand>, DataError.Local> =
        try {
            val entityBrands = brandDao.observeAll().first()
            val brands = entityBrands.map { it.toBrand() }
            Result.Success(brands)
        } catch (e: Exception) {
            Result.Failure(DataError.Local.UNKNOWN)
        }

    override suspend fun getSeries(brandId: String): Result<List<Series>, DataError.Local> =
        try {
            val series = seriesDao.observeByBrand(brandId)
                .first()
                .map { it.toSeries() }
            Result.Success(series)
        } catch (e: Exception) {
            Result.Failure(DataError.Local.UNKNOWN)
        }

    override suspend fun getFeatures(brandId: String): Result<Set<Feature>, DataError.Local> =
        try {
            val features = featureDao.observeByBrand(brandId)
                .first()
                .map { it.toFeature() }
                .toSet()
            Result.Success(features)
        } catch (e: Exception) {
            Result.Failure(DataError.Local.UNKNOWN)
        }
}
