package com.carly.vehicles.data.datasource.catalog

import android.content.Context
import com.carly.vehicles.data.datasource.dto.BrandDto
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogLoader @Inject constructor(
    private val context: Context
) {
    suspend fun loadCatalog(): List<BrandDto> {
        val jsonString = context.assets.open("catalog.json")
            .bufferedReader()
            .use { it.readText() }

        return Json.decodeFromString(jsonString)
    }
}