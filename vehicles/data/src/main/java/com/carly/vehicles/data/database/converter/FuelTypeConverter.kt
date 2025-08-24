package com.carly.vehicles.data.database.converter

import androidx.room.TypeConverter
import com.carly.vehicles.domain.model.FuelType

class FuelTypeConverter {
    
    @TypeConverter
    fun fromFuelType(fuelType: FuelType): String {
        return fuelType.name
    }
    
    @TypeConverter
    fun toFuelType(name: String): FuelType {
        return when (name) {
            "Diesel" -> FuelType.Diesel
            "Gasoline" -> FuelType.Gasoline
            "Hybrid" -> FuelType.Hybrid
            "Electric" -> FuelType.Electric
            else -> FuelType.Other
        }
    }
}