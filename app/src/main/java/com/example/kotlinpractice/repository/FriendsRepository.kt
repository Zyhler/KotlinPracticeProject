package com.example.kotlinpractice.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.kotlinpractice.models.Friend
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FriendsRepository {
    private val baseUrl = "https://birthdaysrest.azurewebsites.net/api/persons/"
    // the specific (collection) part of the URL is on the individual methods in the interface friendstoreService

    //"http://anbo-restserviceproviderfriends.azurewebsites.net/Service1.svc/"
    private val friendBirthdayService: FriendsBirthdayService
    val friendsLiveData: MutableLiveData<List<Friend>> = MutableLiveData<List<Friend>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        //val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // GSON
            //.addConverterFactory(KotlinJsonAdapterFactory)
            //.addConverterFactory(MoshiConverterFactory.create(moshi)) // Moshi, added to Gradle dependencies
            .build()
        friendBirthdayService = build.create(FriendsBirthdayService::class.java)
        getFriends()
    }

    fun getFriends() {
        friendBirthdayService.getAllfriends().enqueue(object : Callback<List<Friend>> {
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    //Log.d("APPLE", response.body().toString())
                    val b: List<Friend>? = response.body()
                    friendsLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                //friendsLiveData.postValue(null)
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun add(friend: Friend) {
        friendBirthdayService.savefriend(friend).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Added: " + response.body())
                    updateMessageLiveData.postValue("Added: " + response.body())
                    getFriends()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun delete(id: Int) {
        friendBirthdayService.deletefriend(id).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Deleted: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun update(friend: Friend) {
        friendBirthdayService.updatefriend(friend.id, friend).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Updated: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun sortByName() {
        friendsLiveData.value = friendsLiveData.value?.sortedBy { it.name }
    }

    fun sortByNameDescending() {
        friendsLiveData.value = friendsLiveData.value?.sortedByDescending { it.name }
    }
    fun filterByName(name: String) {
        if (name.isBlank()) {
            getFriends()
        }else {
            friendsLiveData.value = friendsLiveData.value?.filter { friend -> friend.name.contains(name) }
            // TODO: bug fix: friendsLiveData.value keeps getting smaller for each filter
        }
    }
}