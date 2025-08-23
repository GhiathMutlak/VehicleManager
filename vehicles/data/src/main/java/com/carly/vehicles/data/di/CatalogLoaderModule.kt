package com.carly.vehicles.data.di

import android.content.Context
import com.carly.vehicles.data.datasource.catalog.CatalogLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CatalogLoaderModule {

    @Provides
    @Singleton
    fun provideCatalogLoader(@ApplicationContext context: Context): CatalogLoader =
        CatalogLoader(context)
}