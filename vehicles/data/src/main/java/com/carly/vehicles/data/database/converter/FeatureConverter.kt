package com.carly.vehicles.data.database.converter

import androidx.room.TypeConverter
import com.carly.vehicles.domain.model.Feature

class FeatureConverter {
    
    @TypeConverter
    fun fromFeature(feature: Feature): String {
        return feature.id
    }
    
    @TypeConverter
    fun toFeature(id: String): Feature {
        return Feature.fromId(id)
    }
}