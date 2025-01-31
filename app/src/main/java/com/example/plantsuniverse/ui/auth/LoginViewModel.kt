package com.example.plantsuniverse.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsuniverse.data.users.User
import com.example.plantsuniverse.data.users.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val usersRepository = UserRepository()
    fun register(onFinishUi: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            usersRepository.upsertUser(User.fromFirebaseAuth())
            onFinishUi()
        }
    }
}