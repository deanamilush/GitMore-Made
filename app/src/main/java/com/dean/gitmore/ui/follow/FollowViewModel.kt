package com.dean.gitmore.ui.follow

import androidx.lifecycle.*
import com.dean.core.data.Resource
import com.dean.core.domain.User
import com.dean.core.domain.UserUseCase
import com.dean.gitmore.util.TypeView

class FollowViewModel(userUseCase: UserUseCase) : ViewModel() {
    private var username: MutableLiveData<String> = MutableLiveData()
    private lateinit var typeView: TypeView

    fun setFollow(user: String, type: TypeView) {
        if (username.value == user) {
            return
        }
        username.value = user
        typeView = type
    }

    val favoriteUsers:LiveData<Resource<List<User>>> = Transformations
        .switchMap(username) {
            when (typeView) {
                TypeView.FOLLOWER -> userUseCase.getAllFollowers(it).asLiveData()
                TypeView.FOLLOWING -> userUseCase.getAllFollowing(it).asLiveData()
            }
        }
}