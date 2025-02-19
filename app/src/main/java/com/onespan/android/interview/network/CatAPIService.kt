package com.onespan.android.interview.network

import retrofit2.http.GET
import com.onespan.android.interview.model.CatBreedResponse

interface CatApiService {
    @GET("breeds")
    suspend fun getBreeds(): CatBreedResponse
}
