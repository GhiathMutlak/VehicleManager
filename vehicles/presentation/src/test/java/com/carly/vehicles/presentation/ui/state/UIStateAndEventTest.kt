package com.carly.vehicles.presentation.ui.state

import com.carly.vehicles.domain.model.Brand
import com.carly.vehicles.domain.model.Feature
import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Series
import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.presentation.ui.createvehicle.CreateVehicleAction
import com.carly.vehicles.presentation.ui.createvehicle.CreateVehicleEvent
import com.carly.vehicles.presentation.ui.createvehicle.CreateVehicleState
import com.carly.vehicles.presentation.ui.createvehicle.CreateVehicleStep
import com.carly.vehicles.presentation.ui.dashboard.DashboardState
import com.carly.vehicles.presentation.ui.mycarslist.MyCarsListAction
import com.carly.vehicles.presentation.ui.mycarslist.MyCarsListEvent
import com.carly.vehicles.presentation.ui.mycarslist.MyCarsListState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for UI state classes and event handling testing MVI architecture patterns.
 * 
 * Tests cover:
 * - State class property validation and defaults
 * - State immutability and copy operations
 * - Computed properties and derived state
 * - Event class equality and serialization
 * - Action class validation and type safety
 * - Edge cases and boundary conditions
 */
class UIStateAndEventTest {

    // Sample test data
    private val sampleVehicle = Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel)
    private val sampleBrand = Brand("bmw", "BMW")
    private val sampleSeries = Series("x5", "bmw", "X5", 1999, 2024)
    private val sampleFeature = Feature.fromId("diagnostics", "Diagnostics")

    @Test
    fun `DashboardState should have correct default values`() {
        // Given
        val state = DashboardState()

        // Then
        assertNull("Selected vehicle should be null by default", state.selectedVehicle)
        assertTrue("Features should be empty by default", state.features.isEmpty())
        assertTrue("Should be loading by default", state.isLoading)
    }

    @Test
    fun `DashboardState should support copy operations`() {
        // Given
        val originalState = DashboardState(
            selectedVehicle = sampleVehicle,
            features = setOf(sampleFeature),
            isLoading = false
        )

        // When
        val copiedState = originalState.copy(isLoading = true)

        // Then
        assertEquals("Should preserve selected vehicle", sampleVehicle, copiedState.selectedVehicle)
        assertEquals("Should preserve features", setOf(sampleFeature), copiedState.features)
        assertTrue("Should update loading state", copiedState.isLoading)
        assertNotSame("Should create new instance", originalState, copiedState)
    }

    @Test
    fun `DashboardState should handle large feature sets`() {
        // Given
        val largeFeatureSet = (1..1000).map { 
            Feature.fromId("feature$it", "Feature $it") 
        }.toSet()

        // When
        val state = DashboardState(
            selectedVehicle = sampleVehicle,
            features = largeFeatureSet,
            isLoading = false
        )

        // Then
        assertEquals("Should handle large feature sets", 1000, state.features.size)
        assertEquals("Should preserve vehicle", sampleVehicle, state.selectedVehicle)
        assertFalse("Should not be loading", state.isLoading)
    }

    @Test
    fun `MyCarsListState should have correct default values`() {
        // Given
        val state = MyCarsListState()

        // Then
        assertFalse("Should not be loading by default", state.isLoading)
        assertTrue("Vehicle list should be empty by default", state.vehicles.isEmpty())
        assertNull("No vehicle should be selected by default", state.selectedVehicleId)
    }

    @Test
    fun `MyCarsListState should support copy operations with vehicle list`() {
        // Given
        val vehicles = listOf(sampleVehicle, Vehicle(2L, "Audi", "A4", 2019, FuelType.Gasoline))
        val originalState = MyCarsListState(
            isLoading = true,
            vehicles = vehicles,
            selectedVehicleId = 1L
        )

        // When
        val copiedState = originalState.copy(isLoading = false, selectedVehicleId = 2L)

        // Then
        assertEquals("Should preserve vehicle list", vehicles, copiedState.vehicles)
        assertFalse("Should update loading state", copiedState.isLoading)
        assertEquals("Should update selected ID", 2L, copiedState.selectedVehicleId)
    }

    @Test
    fun `MyCarsListState should handle empty and large vehicle lists`() {
        // Given
        val emptyState = MyCarsListState(vehicles = emptyList())
        val largeVehicleList = (1..500).map { id ->
            Vehicle(id.toLong(), "Brand$id", "Series$id", 2000 + (id % 25), FuelType.Gasoline)
        }
        val largeState = MyCarsListState(vehicles = largeVehicleList)

        // Then
        assertTrue("Should handle empty list", emptyState.vehicles.isEmpty())
        assertEquals("Should handle large list", 500, largeState.vehicles.size)
        assertFalse("Both should not be loading by default", emptyState.isLoading || largeState.isLoading)
    }

    @Test
    fun `CreateVehicleState should have correct default values`() {
        // Given
        val state = CreateVehicleState()

        // Then
        assertEquals("Should start at brand selection", CreateVehicleStep.BrandSelection, state.currentStep)
        assertNull("No brand should be selected", state.selectedBrand)
        assertNull("No series should be selected", state.selectedSeries)
        assertNull("No year should be selected", state.selectedYear)
        assertNull("No fuel type should be selected", state.selectedFuelType)
        assertTrue("Brands should be empty", state.brands.isEmpty())
        assertTrue("Series should be empty", state.series.isEmpty())
        assertTrue("Years should be empty", state.availableYears.isEmpty())
        assertTrue("Search query should be empty", state.searchQuery.isEmpty())
        assertFalse("Should not be loading", state.isLoading)
    }

    @Test
    fun `CreateVehicleState filtered properties should work correctly`() {
        // Given
        val brands = listOf(
            Brand("bmw", "BMW"),
            Brand("audi", "Audi"),
            Brand("mercedes", "Mercedes-Benz")
        )
        val state = CreateVehicleState(
            brands = brands,
            searchQuery = "BM"
        )

        // When
        val filteredBrands = state.filteredBrands

        // Then
        assertEquals("Should filter brands by search query", 1, filteredBrands.size)
        assertEquals("Should contain BMW", "BMW", filteredBrands[0].name)
    }

    @Test
    fun `CreateVehicleState filtered properties should be case insensitive`() {
        // Given
        val brands = listOf(Brand("bmw", "BMW"))
        val state = CreateVehicleState(
            brands = brands,
            searchQuery = "bmw"
        )

        // When
        val filteredBrands = state.filteredBrands

        // Then
        assertEquals("Should find BMW with lowercase search", 1, filteredBrands.size)
        assertEquals("Should contain BMW", "BMW", filteredBrands[0].name)
    }

    @Test
    fun `CreateVehicleState filtered series should work correctly`() {
        // Given
        val series = listOf(
            Series("x5", "bmw", "X5", 1999, 2024),
            Series("x3", "bmw", "X3", 2003, 2024),
            Series("3-series", "bmw", "3 Series", 1975, 2024)
        )
        val state = CreateVehicleState(
            series = series,
            searchQuery = "X"
        )

        // When
        val filteredSeries = state.filteredSeries

        // Then
        assertEquals("Should filter series containing X", 2, filteredSeries.size)
        assertTrue("Should contain X5", filteredSeries.any { it.name == "X5" })
        assertTrue("Should contain X3", filteredSeries.any { it.name == "X3" })
        assertFalse("Should not contain 3 Series", filteredSeries.any { it.name == "3 Series" })
    }

    @Test
    fun `CreateVehicleState filtered years should work correctly`() {
        // Given
        val years = listOf(2024, 2023, 2022, 2021, 2020)
        val state = CreateVehicleState(
            availableYears = years,
            searchQuery = "202"
        )

        // When
        val filteredYears = state.filteredYears

        // Then
        assertEquals("Should filter years containing 202", 5, filteredYears.size)
        assertTrue("Should contain all 202x years", filteredYears.containsAll(years))
    }

    @Test
    fun `CreateVehicleState filtered fuel types should work correctly`() {
        // Given
        val state = CreateVehicleState(searchQuery = "Electric")

        // When
        val filteredFuelTypes = state.filteredFuelTypes

        // Then
        assertTrue("Should contain Electric", filteredFuelTypes.contains(FuelType.Electric))
        assertFalse("Should not contain Diesel", filteredFuelTypes.contains(FuelType.Diesel))
        assertFalse("Should not contain Gasoline", filteredFuelTypes.contains(FuelType.Gasoline))
        assertEquals("Should only contain Electric", 1, filteredFuelTypes.size)
    }

    @Test
    fun `CreateVehicleState search placeholder should be correct for each step`() {
        // Given & When & Then
        val brandState = CreateVehicleState(currentStep = CreateVehicleStep.BrandSelection)
        assertEquals("Should have brand placeholder", "Search for brand ...", brandState.searchPlaceholder)

        val seriesState = CreateVehicleState(currentStep = CreateVehicleStep.SeriesSelection)
        assertEquals("Should have series placeholder", "Search for series ...", seriesState.searchPlaceholder)

        val yearState = CreateVehicleState(currentStep = CreateVehicleStep.YearSelection)
        assertEquals("Should have year placeholder", "Search for build year ...", yearState.searchPlaceholder)

        val fuelState = CreateVehicleState(currentStep = CreateVehicleStep.FuelTypeSelection)
        assertEquals("Should have fuel type placeholder", "Search for fuel type ...", fuelState.searchPlaceholder)
    }

    @Test
    fun `CreateVehicleState breadcrumb should build correctly`() {
        // Given
        val state = CreateVehicleState(
            selectedBrand = Brand("bmw", "BMW"),
            selectedSeries = Series("x5", "bmw", "X5", 1999, 2024),
            selectedYear = 2020
        )

        // When
        val breadcrumb = state.breadcrumb

        // Then
        assertEquals("Should build correct breadcrumb", "BMW, X5, 2020", breadcrumb)
    }

    @Test
    fun `CreateVehicleState breadcrumb should handle partial selections`() {
        // Given
        val brandOnly = CreateVehicleState(selectedBrand = Brand("bmw", "BMW"))
        val brandAndSeries = CreateVehicleState(
            selectedBrand = Brand("bmw", "BMW"),
            selectedSeries = Series("x5", "bmw", "X5", 1999, 2024)
        )

        // When & Then
        assertEquals("Should show brand only", "BMW", brandOnly.breadcrumb)
        assertEquals("Should show brand and series", "BMW, X5", brandAndSeries.breadcrumb)
    }

    @Test
    fun `CreateVehicleState canNavigateForward should be correct`() {
        // Given
        val brandState = CreateVehicleState(
            currentStep = CreateVehicleStep.BrandSelection,
            selectedBrand = sampleBrand
        )
        val seriesState = CreateVehicleState(
            currentStep = CreateVehicleStep.SeriesSelection,
            selectedSeries = sampleSeries
        )
        val yearState = CreateVehicleState(
            currentStep = CreateVehicleStep.YearSelection,
            selectedYear = 2020
        )
        val fuelState = CreateVehicleState(
            currentStep = CreateVehicleStep.FuelTypeSelection,
            selectedFuelType = FuelType.Diesel
        )

        // Then
        assertTrue("Should allow forward from brand selection", brandState.canNavigateForward)
        assertTrue("Should allow forward from series selection", seriesState.canNavigateForward)
        assertTrue("Should allow forward from year selection", yearState.canNavigateForward)
        assertTrue("Should allow forward from fuel selection", fuelState.canNavigateForward)
    }

    @Test
    fun `CreateVehicleState canNavigateForward should be false without selection`() {
        // Given
        val emptyStates = listOf(
            CreateVehicleState(currentStep = CreateVehicleStep.BrandSelection),
            CreateVehicleState(currentStep = CreateVehicleStep.SeriesSelection),
            CreateVehicleState(currentStep = CreateVehicleStep.YearSelection),
            CreateVehicleState(currentStep = CreateVehicleStep.FuelTypeSelection)
        )

        // Then
        emptyStates.forEach { state ->
            assertFalse("Should not allow forward navigation without selection", state.canNavigateForward)
        }
    }

    @Test
    fun `MyCarsListEvent should have correct equality and type checking`() {
        // Given
        val error1 = MyCarsListEvent.Error("Test error")
        val error2 = MyCarsListEvent.Error("Test error")
        val error3 = MyCarsListEvent.Error("Different error")
        val navigate1 = MyCarsListEvent.NavigateToDashboard
        val navigate2 = MyCarsListEvent.NavigateToDashboard
        val addVehicle = MyCarsListEvent.NavigateToAddVehicle

        // Then
        assertEquals("Same error messages should be equal", error1, error2)
        assertNotEquals("Different error messages should not be equal", error1, error3)
        assertEquals("Navigation events should be equal", navigate1, navigate2)
        assertNotEquals("Different navigation events should not be equal", navigate1, addVehicle)
    }

    @Test
    fun `CreateVehicleEvent should have correct equality and type checking`() {
        // Given
        val error1 = CreateVehicleEvent.Error("Test error")
        val error2 = CreateVehicleEvent.Error("Test error")
        val error3 = CreateVehicleEvent.Error("Different error")
        val created1 = CreateVehicleEvent.VehicleCreated(123L)
        val created2 = CreateVehicleEvent.VehicleCreated(123L)
        val created3 = CreateVehicleEvent.VehicleCreated(456L)
        val navigate1 = CreateVehicleEvent.NavigateBack
        val navigate2 = CreateVehicleEvent.NavigateBack

        // Then
        assertEquals("Same error messages should be equal", error1, error2)
        assertNotEquals("Different error messages should not be equal", error1, error3)
        assertEquals("Same vehicle IDs should be equal", created1, created2)
        assertNotEquals("Different vehicle IDs should not be equal", created1, created3)
        assertEquals("Navigation events should be equal", navigate1, navigate2)
    }

    @Test
    fun `MyCarsListAction should have correct equality and type checking`() {
        // Given
        val select1 = MyCarsListAction.SelectVehicle(123L)
        val select2 = MyCarsListAction.SelectVehicle(123L)
        val select3 = MyCarsListAction.SelectVehicle(456L)
        val delete1 = MyCarsListAction.DeleteVehicle(123L)
        val delete2 = MyCarsListAction.DeleteVehicle(123L)
        val add1 = MyCarsListAction.AddNewVehicle
        val add2 = MyCarsListAction.AddNewVehicle

        // Then
        assertEquals("Same select actions should be equal", select1, select2)
        assertNotEquals("Different vehicle IDs should not be equal", select1, select3)
        assertEquals("Same delete actions should be equal", delete1, delete2)
        assertNotEquals("Select and delete should not be equal", select1, delete1)
        assertEquals("Add vehicle actions should be equal", add1, add2)
    }

    @Test
    fun `CreateVehicleAction should have correct equality and type checking`() {
        // Given
        val search1 = CreateVehicleAction.UpdateSearchQuery("BMW")
        val search2 = CreateVehicleAction.UpdateSearchQuery("BMW")
        val search3 = CreateVehicleAction.UpdateSearchQuery("Audi")
        val selectBrand1 = CreateVehicleAction.SelectBrand(sampleBrand)
        val selectBrand2 = CreateVehicleAction.SelectBrand(sampleBrand)
        val selectSeries = CreateVehicleAction.SelectSeries(sampleSeries)
        val selectYear1 = CreateVehicleAction.SelectYear(2020)
        val selectYear2 = CreateVehicleAction.SelectYear(2020)
        val selectFuel = CreateVehicleAction.SelectFuelType(FuelType.Diesel)

        // Then
        assertEquals("Same search queries should be equal", search1, search2)
        assertNotEquals("Different search queries should not be equal", search1, search3)
        assertEquals("Same brand selections should be equal", selectBrand1, selectBrand2)
        assertEquals("Same year selections should be equal", selectYear1, selectYear2)
        assertNotEquals("Different action types should not be equal", selectBrand1, selectSeries)
        assertNotEquals("Different action types should not be equal", selectYear1, selectFuel)
    }

    @Test
    fun `State classes should handle null and edge values gracefully`() {
        // Given
        val dashboardState = DashboardState(
            selectedVehicle = null,
            features = emptySet(),
            isLoading = false
        )
        val carsListState = MyCarsListState(
            isLoading = false,
            vehicles = emptyList(),
            selectedVehicleId = null
        )
        val createState = CreateVehicleState(
            selectedBrand = null,
            selectedSeries = null,
            selectedYear = null,
            selectedFuelType = null
        )

        // Then - Should not throw exceptions
        assertNull("Dashboard vehicle should be null", dashboardState.selectedVehicle)
        assertTrue("Dashboard features should be empty", dashboardState.features.isEmpty())
        assertTrue("Cars list should be empty", carsListState.vehicles.isEmpty())
        assertNull("Selected vehicle ID should be null", carsListState.selectedVehicleId)
        assertNull("Selected brand should be null", createState.selectedBrand)
        assertTrue("Create state breadcrumb should be empty", createState.breadcrumb.isEmpty())
    }

    @Test
    fun `State classes should support deep copying with nested collections`() {
        // Given
        val features = setOf(sampleFeature, Feature.fromId("live-data", "Live Data"))
        val vehicles = listOf(sampleVehicle, Vehicle(2L, "Audi", "A4", 2019, FuelType.Gasoline))
        val brands = listOf(sampleBrand, Brand("audi", "Audi"))

        val originalDashboard = DashboardState(features = features)
        val originalCarsList = MyCarsListState(vehicles = vehicles)
        val originalCreate = CreateVehicleState(brands = brands)

        // When
        val copiedDashboard = originalDashboard.copy(isLoading = false)
        val copiedCarsList = originalCarsList.copy(isLoading = true)
        val copiedCreate = originalCreate.copy(searchQuery = "test")

        // Then
        assertEquals("Should preserve features", features, copiedDashboard.features)
        assertEquals("Should preserve vehicles", vehicles, copiedCarsList.vehicles)
        assertEquals("Should preserve brands", brands, copiedCreate.brands)
        assertNotSame("Should be different instances", originalDashboard, copiedDashboard)
        assertNotSame("Should be different instances", originalCarsList, copiedCarsList)
        assertNotSame("Should be different instances", originalCreate, copiedCreate)
    }
}