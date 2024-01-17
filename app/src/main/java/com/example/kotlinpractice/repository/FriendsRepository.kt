package com.example.kotlinpractice.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.kotlinpractice.models.Friend
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.auth.FirebaseAuth

class FriendsRepository {
    private val baseUrl = "https://birthdaysrest.azurewebsites.net/api/"
    private val friendBirthdayService: FriendsBirthdayService
    val friendsLiveData: MutableLiveData<List<Friend>> = MutableLiveData<List<Friend>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val userEmail = FirebaseAuth.getInstance().currentUser?.email

    init {

        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // GSON
            //.addConverterFactory(KotlinJsonAdapterFactory)
            .build()
        friendBirthdayService = build.create(FriendsBirthdayService::class.java)
        getFriends()
    }

    fun getFriends() {
        friendBirthdayService.getAllfriends().enqueue(object : Callback<List<Friend>> {
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    val friends: List<Friend>? = response.body()
                    val yourFriends: MutableList<Friend> = mutableListOf()

                    if (friends != null) {
                        for(item in friends){
                            if(item.userId == userEmail)
                                yourFriends.add(item)
                        }
                    }
                    friendsLiveData.postValue(yourFriends!!)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("Repository", message)
                }
            }

            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                //friendsLiveData.postValue(null)
                errorMessageLiveData.postValue(t.message)
                Log.d("Repository", t.message!!)
            }
        })
    }
    fun getFriends2() {
        friendBirthdayService.getAllfriends().enqueue(object : Callback<List<Friend>> {
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    val friends: List<Friend>? = response.body()

                    friendsLiveData.postValue(friends!!)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("Repository", message)
                }
            }

            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                //friendsLiveData.postValue(null)
                errorMessageLiveData.postValue(t.message)
                Log.d("Repository", t.message!!)
            }
        })
    }

    fun add(friend: Friend) {
        friendBirthdayService.savefriend(friend).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                if (response.isSuccessful) {
                    Log.d("Repository", "Added: " + response.body())
                    updateMessageLiveData.postValue("Added: " + response.body())
                    getFriends()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("Repository", message)
                }
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("Repository", t.message!!)
            }
        })
    }

    fun delete(id: Int) {
        friendBirthdayService.deletefriend(id).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                if (response.isSuccessful) {
                    Log.d("Repository", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Deleted: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("Repository", message)
                }
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("Repository", t.message!!)
            }
        })
    }

    fun update(friend: Friend) {
        friendBirthdayService.updatefriend(friend.id, friend).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {

                if (response.isSuccessful) {
                    Log.d("Repository", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Updated: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("Repository", message)
                }
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("Repository", t.message!!)
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