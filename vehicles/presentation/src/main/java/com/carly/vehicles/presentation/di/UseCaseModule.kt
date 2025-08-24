package com.carly.vehicles.presentation.di

import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.repo.VehicleSelectionRepository
import com.carly.vehicles.domain.usecase.DeleteVehicleUseCase
import com.carly.vehicles.domain.usecase.SetSelectedVehicleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideDeleteVehicle(
        vehicleRepo: VehicleRepo,
        vehicleSelectionRepository: VehicleSelectionRepository
    ): DeleteVehicleUseCase = DeleteVehicleUseCase(vehicleRepo, vehicleSelectionRepository)

    @Provides
    @Singleton
    fun provideSetSelectedVehicle(
        vehicleSelectionRepository: VehicleSelectionRepository
    ): SetSelectedVehicleUseCase = SetSelectedVehicleUseCase(vehicleSelectionRepository)
}