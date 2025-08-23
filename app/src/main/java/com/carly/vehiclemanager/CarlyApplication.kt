package com.carly.vehiclemanager

import android.app.Application
import com.carly.vehicles.data.datasource.catalog.CatalogSeeder
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class CarlyApplication: Application() {
    @Inject lateinit var catalogSeeder: CatalogSeeder

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            catalogSeeder.seedIfNeeded()
        }
    }
}