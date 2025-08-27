package com.carly.vehicles.domain.util

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Result utility class testing functional programming patterns.
 * 
 * Tests cover:
 * - Success and Failure creation
 * - Map operations and transformations
 * - onSuccess and onFailure callbacks
 * - asEmptyResult conversion
 * - Proper type safety and behavior
 */
class ResultTest {

    // Test error for testing purposes
    private data class TestError(val message: String) : Error

    @Test
    fun `Result Success should contain data`() {
        // Given
        val result = Result.Success("test data")

        // Then
        assertTrue("Should be Success", result is Result.Success)
        assertEquals("test data", result.data)
    }

    @Test
    fun `Result Failure should contain error`() {
        // Given
        val error = TestError("test error")
        val result = Result.Failure(error)

        // Then
        assertTrue("Should be Failure", result is Result.Failure)
        assertEquals(error, result.error)
    }

    @Test
    fun `map should transform Success data`() {
        // Given
        val result = Result.Success(5)

        // When
        val mapped = result.map { it * 2 }

        // Then
        assertTrue("Should remain Success", mapped is Result.Success)
        assertEquals(10, (mapped as Result.Success).data)
    }

    @Test
    fun `map should preserve Failure`() {
        // Given
        val error = TestError("test error")
        val result: Result<Int, TestError> = Result.Failure(error)

        // When
        val mapped = result.map { it * 2 }

        // Then
        assertTrue("Should remain Failure", mapped is Result.Failure)
        assertEquals(error, (mapped as Result.Failure).error)
    }

    @Test
    fun `map should change data type`() {
        // Given
        val result = Result.Success(42)

        // When
        val mapped = result.map { "Number: $it" }

        // Then
        assertTrue("Should remain Success", mapped is Result.Success)
        assertEquals("Number: 42", (mapped as Result.Success).data)
    }

    @Test
    fun `onSuccess should execute callback for Success`() {
        // Given
        var callbackExecuted = false
        var receivedData: String? = null
        val result = Result.Success("test data")

        // When
        val returnedResult = result.onSuccess { data ->
            callbackExecuted = true
            receivedData = data
        }

        // Then
        assertTrue("Callback should be executed", callbackExecuted)
        assertEquals("test data", receivedData)
        assertSame("Should return same instance", result, returnedResult)
    }

    @Test
    fun `onSuccess should not execute callback for Failure`() {
        // Given
        var callbackExecuted = false
        val error = TestError("test error")
        val result: Result<String, TestError> = Result.Failure(error)

        // When
        val returnedResult = result.onSuccess { 
            callbackExecuted = true
        }

        // Then
        assertFalse("Callback should not be executed", callbackExecuted)
        assertSame("Should return same instance", result, returnedResult)
    }

    @Test
    fun `onFailure should execute callback for Failure`() {
        // Given
        var callbackExecuted = false
        var receivedError: TestError? = null
        val error = TestError("test error")
        val result: Result<String, TestError> = Result.Failure(error)

        // When
        val returnedResult = result.onFailure { err ->
            callbackExecuted = true
            receivedError = err
        }

        // Then
        assertTrue("Callback should be executed", callbackExecuted)
        assertEquals(error, receivedError)
        assertSame("Should return same instance", result, returnedResult)
    }

    @Test
    fun `onFailure should not execute callback for Success`() {
        // Given
        var callbackExecuted = false
        val result = Result.Success("test data")

        // When
        val returnedResult = result.onFailure { 
            callbackExecuted = true
        }

        // Then
        assertFalse("Callback should not be executed", callbackExecuted)
        assertSame("Should return same instance", result, returnedResult)
    }

    @Test
    fun `asEmptyResult should convert Success to Unit`() {
        // Given
        val result = Result.Success("some data")

        // When
        val emptyResult = result.asEmptyResult()

        // Then
        assertTrue("Should be Success", emptyResult is Result.Success)
        assertEquals(Unit, (emptyResult as Result.Success).data)
    }

    @Test
    fun `asEmptyResult should preserve Failure`() {
        // Given
        val error = TestError("test error")
        val result: Result<String, TestError> = Result.Failure(error)

        // When
        val emptyResult = result.asEmptyResult()

        // Then
        assertTrue("Should remain Failure", emptyResult is Result.Failure)
        assertEquals(error, (emptyResult as Result.Failure).error)
    }

    @Test
    fun `chaining operations should work correctly`() {
        // Given
        var successCallbackCalled = false
        var failureCallbackCalled = false
        val result = Result.Success(10)

        // When
        val finalResult = result
            .map { it * 2 }
            .onSuccess { successCallbackCalled = true }
            .onFailure { failureCallbackCalled = true }
            .map { "Result: $it" }

        // Then
        assertTrue("Success callback should be called", successCallbackCalled)
        assertFalse("Failure callback should not be called", failureCallbackCalled)
        assertTrue("Should be Success", finalResult is Result.Success)
        assertEquals("Result: 20", (finalResult as Result.Success).data)
    }

    @Test
    fun `chaining operations with Failure should preserve error`() {
        // Given
        var successCallbackCalled = false
        var failureCallbackCalled = false
        val error = TestError("original error")
        val result: Result<Int, TestError> = Result.Failure(error)

        // When
        val finalResult = result
            .map { it * 2 }
            .onSuccess { successCallbackCalled = true }
            .onFailure { failureCallbackCalled = true }
            .map { "Result: $it" }

        // Then
        assertFalse("Success callback should not be called", successCallbackCalled)
        assertTrue("Failure callback should be called", failureCallbackCalled)
        assertTrue("Should remain Failure", finalResult is Result.Failure)
        assertEquals(error, (finalResult as Result.Failure).error)
    }

    @Test
    fun `EmptyResult type alias should work correctly`() {
        // Given
        val successResult: EmptyResult<TestError> = Result.Success(Unit)
        val failureResult: EmptyResult<TestError> = Result.Failure(TestError("error"))

        // Then
        assertTrue("Success EmptyResult should be Success", successResult is Result.Success)
        assertTrue("Failure EmptyResult should be Failure", failureResult is Result.Failure)
        assertEquals(Unit, (successResult as Result.Success).data)
    }

    @Test
    fun `Result should maintain type safety`() {
        // Given
        val stringResult: Result<String, TestError> = Result.Success("hello")
        val intResult: Result<Int, TestError> = Result.Success(42)

        // When
        val transformedString = stringResult.map { it.uppercase() }
        val transformedInt = intResult.map { it + 1 }

        // Then
        assertEquals("HELLO", (transformedString as Result.Success).data)
        assertEquals(43, (transformedInt as Result.Success).data)
    }
}