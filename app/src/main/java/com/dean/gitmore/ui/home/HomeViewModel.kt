package com.dean.gitmore.ui.home

import androidx.lifecycle.*
import com.bumptech.glide.load.engine.Resource
import com.dean.core.domain.UserUseCase

class HomeViewModel(userUseCase: UserUseCase) : ViewModel() {
    private var username: MutableLiveData<String> = MutableLiveData()

    fun setSearch(query: String) {
        if (username.value == query) {
            return
        }
        username.value = query
    }

    val users: LiveData<Resource<List<User>>> = Transformations
        .switchMap(username) {
            userUseCase.getAllUsers(it).asLiveData()
        }
}