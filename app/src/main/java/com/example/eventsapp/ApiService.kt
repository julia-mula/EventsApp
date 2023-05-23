package com.example.eventsapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/users/{userId}/events")
    fun getUserEvents(@Path("userId") userId: Int): Call<ArrayList<Event>>

    @GET("/users/{userId}/generalEvents")
    fun getGeneralEvents(@Path("userId") userId: Int): Call<ArrayList<Event>>
}