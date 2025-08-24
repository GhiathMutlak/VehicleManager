package com.carly.vehicles.data.repo.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.carly.vehicles.domain.repo.VehicleSelectionRepository
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.EmptyResult
import com.carly.vehicles.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val SELECTED_ID = longPreferencesKey("selected_vehicle_id")

class DataStoreSelectionRepo @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : VehicleSelectionRepository {

    // Flow of currently selected vehicle ID, emits null if none selected
    override val selectedVehicleId: Flow<Long?> = dataStore.data
        .map { prefs -> prefs[SELECTED_ID] }

    // Updates the selected vehicle ID, returning EmptyResult
    override suspend fun setSelectedVehicleId(id: Long?): EmptyResult<DataError.Local> {
        return try {
            dataStore.edit { prefs ->
                if (id == null) prefs.remove(SELECTED_ID)
                else prefs[SELECTED_ID] = id
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            // Catch disk errors or any DataStore failure
            Result.Failure(DataError.Local.DISK_FULL)
        }
    }
}
