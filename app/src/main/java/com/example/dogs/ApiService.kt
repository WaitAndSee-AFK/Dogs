package com.example.dogs

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

fun interface ApiService {

    @GET("random")
    fun loadDogImage(): Single<DogImage>
}