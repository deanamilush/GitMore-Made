package com.dean.gitmore.detail

import androidx.lifecycle.*
import com.dean.core.domain.User
import com.dean.core.domain.UserUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    //val favorite: MutableLiveData<Boolean> = MutableLiveData()

    fun detailUsers(username: String) = userUseCase.getDetailUser(username).asLiveData()

    fun getDetailState(username: String) = userUseCase.getDetailState(username)?.asLiveData()

    fun insertFavorite(user: User) = viewModelScope.launch {
        userUseCase.insertUser(user)
        //favorite.value = true
    }

    fun deleteFavorite(user: User) = viewModelScope.launch {
        userUseCase.deleteUser(user)
        //favorite.value = false
    }

    //val isFavorite: LiveData<Boolean> = favorite
}