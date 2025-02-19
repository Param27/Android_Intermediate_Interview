package com.onespan.android.interview.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatBreedResponse(
    @Json(name = "data") val breeds: List<CatBreedModel>
)

@JsonClass(generateAdapter = true)
data class CatBreedModel(
    @Json(name = "breed") val name: String,
    @Json(name = "origin") val origin: String,
    @Json(name = "country") val country: String,
    @Json(name = "coat") val coat: String,
    @Json(name = "pattern") val pattern: String
)
