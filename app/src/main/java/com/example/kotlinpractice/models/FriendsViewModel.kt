package com.example.kotlinpractice.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinpractice.repository.FriendsRepository

class FriendsViewModel : ViewModel() {
    private val repository = FriendsRepository()
    val friendsLiveData: LiveData<List<Friend>> = repository.friendsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        reload()
    }

    fun reload() {
        repository.getFriends()
    }

    operator fun get(index: Int): Friend? {
        return friendsLiveData.value?.get(index)
    }

    fun add(friend: Friend) {
         repository.add(friend)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun update(friend: Friend) {
        repository.update(friend)
    }

    fun sortByTitle() {
        repository.sortByName()
    }

    fun sortByTitleDescending() {
        repository.sortByNameDescending()
    }
    fun filterByName(name: String) {
        repository.filterByName(name)
    }
}