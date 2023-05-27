package com.example.eventsapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("/users/{userId}/events")
    fun getUserEvents(@Path("userId") userId: Int): Call<ArrayList<Event>>

    @POST("/users/{userId}/events")
    fun createUserEvent(@Path("userId") userId: Int, @Body event: NewEvent): Call<Event>

    @Headers("Content-Type: application/json")
    @POST("/login")
    fun loginUser(@Body loginBody: LoginJson): Call<LoginResponse>

}