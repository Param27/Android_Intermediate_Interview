package com.onespan.android.interview

import com.onespan.android.interview.model.CatBreedModel
import com.onespan.android.interview.repository.CatRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CatRepositoryTest {

    private val repository = mock<CatRepository>()

    @Test
    fun `fetchBreeds returns list of cat breeds wrapped in Result`() = runBlocking {
        // Mock data that the repository will return
        val mockBreeds = listOf(CatBreedModel("Persian", "Iran", "Iran", "bi","strips"), CatBreedModel("Maine Coon", "USA","USA", "black","plain"))

        // Wrap the mock result in Result.success()
        whenever(repository.fetchBreeds()).thenReturn(Result.success(mockBreeds))

        // Call the function and verify the result
        val result = repository.fetchBreeds()

        // Verify the result is a success with the expected data
        assertEquals(Result.success(mockBreeds), result)
    }
}
