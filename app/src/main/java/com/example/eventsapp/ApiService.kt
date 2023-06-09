package com.example.eventsapp

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @GET("/users/{userId}/events")
    fun getUserEvents(@Path("userId") userId: Int): Call<ArrayList<Event>>

    @GET("/nearby_events")
    fun getGeneralEvents(): Call<ArrayList<GeneralEvent>>

    @POST("/users/{userId}/events")
    fun createUserEvent(@Path("userId") userId: Int, @Body event: NewEvent): Call<Event>

    @GET("/random_event")
    fun getRandomEvent(): Call<GeneralEvent>

    @Headers("Content-Type: application/json")
    @POST("/login")
    fun loginUser(@Body loginBody: LoginJson): Call<LoginResponse>

    @Multipart
    @POST("/upload")
    fun uploadImage(@Part file: MultipartBody.Part): Call<UploadResponse>

}