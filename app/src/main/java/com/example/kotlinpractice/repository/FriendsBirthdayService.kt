package com.example.kotlinpractice.repository

import com.example.kotlinpractice.models.Friend
import retrofit2.Call
import retrofit2.http.*

// The methods in this interface are attributed with the specific (collection related) part of the URL
// The base URL is found in the class friendsRepository

interface FriendsBirthdayService {
    @GET("persons")
    fun getAllfriends(): Call<List<Friend>>

    @GET("friends/{friendId}")
    fun getfriendById(@Path("friendId") friendId: Int): Call<Friend>

    @POST("friends")
    fun savefriend(@Body friend: Friend): Call<Friend>

    @DELETE("friends/{id}")
    fun deletefriend(@Path("id") id: Int): Call<Friend>

    @PUT("friends/{id}")
    fun updatefriend(@Path("id") id: Int, @Body friend: Friend): Call<Friend>
}