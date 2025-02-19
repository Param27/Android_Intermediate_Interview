package com.onespan.android.interview.repository
import com.onespan.android.interview.model.CatBreedModel
import com.onespan.android.interview.network.CatApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

open class CatRepository {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val api = Retrofit.Builder()
        .baseUrl("https://catfact.ninja/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(CatApiService::class.java)

   open  suspend fun fetchBreeds(): Result<List<CatBreedModel>> {
        return try {
            val response = api.getBreeds().breeds
            Result.success(response)
              }
        catch (e: IOException) {

            Result.failure(e)
        } catch (e: HttpException) {

            Result.failure(e)
        } catch (e: Exception) {
            // Handle unexpected errors
            Result.failure(e)
        }
    }

}
