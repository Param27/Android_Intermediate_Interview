package com.onespan.android.interview

import com.onespan.android.interview.model.CatBreedModel
import com.onespan.android.interview.repository.CatRepository
import com.onespan.android.interview.viewmodel.CatBreedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before

import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
class CatViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: CatRepository
    private lateinit var viewModel: CatBreedViewModel
    private lateinit var connectivityChecker: ConnectivityChecker

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set the test dispatcher
        repository = mock()
        connectivityChecker = mock() // Mock the connectivity checker
        viewModel = CatBreedViewModel(repository, testDispatcher, connectivityChecker)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset dispatcher
    }

    @Test
    fun `fetchBreeds updates state with cat breeds`() = runTest(testDispatcher) {
        val mockBreeds =
            listOf(CatBreedModel("Australian Mist", "Natural", "Australia", "Natural", "White"))
        whenever(repository.fetchBreeds()).thenReturn(Result.success(mockBreeds))
        whenever(connectivityChecker.isInternetAvailable()).thenReturn(true) // Internet available

        viewModel.fetchBreeds()
        advanceUntilIdle() // Ensure all coroutines complete

        assertEquals(mockBreeds, viewModel.breeds.first())
        assertFalse(viewModel.isLoading.value)

    }

    @Test
    fun `fetchBreeds handles network error`() = runTest {
        // Arrange
        val errorMessage = "Network Error"
        whenever(repository.fetchBreeds()).thenThrow(RuntimeException(errorMessage))
        whenever(connectivityChecker.isInternetAvailable()).thenReturn(true) // Internet available

        // Act
        viewModel.fetchBreeds()
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.isLoading.value) // Still loading because the error is handled within the try-catch
        assertEquals(errorMessage, viewModel.errorMessage.value)
        assertTrue(viewModel.breeds.value.isEmpty()) // breeds should be empty
    }

    @Test
    fun `fetchBreeds handles no internet`() = runTest {
        // Arrange
        whenever(connectivityChecker.isInternetAvailable()).thenReturn(false) // No internet

        // Act
        viewModel.fetchBreeds()
        advanceUntilIdle()

        // Assert
        assertFalse(viewModel.isLoading.value)
        assertEquals("No internet connection", viewModel.errorMessage.value)
        assertTrue(viewModel.breeds.value.isEmpty())
    }
}