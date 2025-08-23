package com.carly.vehicles.domain.usecase

import com.carly.vehicles.domain.model.Dashboard
import com.carly.vehicles.domain.repo.CatalogRepo
import com.carly.vehicles.domain.repo.SelectionRepo
import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.util.Result.Failure
import com.carly.vehicles.domain.util.Result.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.let


class ObserveDashboard(
    private val vehicles: VehicleRepo,
    private val selection: SelectionRepo,
    private val catalog: CatalogRepo
) {
    fun stream(): Flow<Dashboard> = combine(
        vehicles.observeVehicles(), selection.selectedVehicleId
    ) { vehicles, selectedId ->
        val selected = vehicles.firstOrNull { it.id == selectedId } ?: vehicles.firstOrNull()
        val features = selected?.let {
            val result = catalog.getFeatures(it.brand)
            when(result) {
                is Failure -> emptySet()
                is Success -> result.data
            }
        } ?: emptySet()
        Dashboard(vehicles, selected, features)
    }
}