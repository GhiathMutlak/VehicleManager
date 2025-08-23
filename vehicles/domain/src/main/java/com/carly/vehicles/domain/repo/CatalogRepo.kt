package com.carly.vehicles.domain.repo

import com.carly.vehicles.domain.model.Brand
import com.carly.vehicles.domain.model.Feature
import com.carly.vehicles.domain.model.Series
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.Result

interface CatalogRepo {
    suspend fun getBrands(): Result<List<Brand>, DataError.Local>
    suspend fun getSeries(brandId: String): Result<List<Series>, DataError.Local>
    suspend fun getFeatures(brandId: String): Result<Set<Feature>, DataError.Local>
}