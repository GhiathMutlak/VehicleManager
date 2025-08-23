package com.carly.vehicles.domain.model

sealed class Feature(val id: String, val displayName: String) {
    object Diagnostics : Feature("diagnostics", "Diagnostics")
    object LiveData : Feature("live_data", "Live Data")
    object BatteryCheck : Feature("battery_check", "Battery Check")
    object CarCheck : Feature("car_check", "Car Check")

    data class Custom(val customId: String, val customName: String) :
        Feature(customId, customName)

    companion object {
        fun fromId(id: String, name: String? = null): Feature =
            when (id) {
                "diagnostics" -> Diagnostics
                "live_data" -> LiveData
                "battery_check" -> BatteryCheck
                "car_check" -> CarCheck
                else -> Custom(id, name ?: id)
            }
    }
}
