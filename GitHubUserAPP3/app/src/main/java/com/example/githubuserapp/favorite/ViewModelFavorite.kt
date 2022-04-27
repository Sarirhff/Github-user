package com.example.githubuserapp.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuserapp.database.DatabaseUser
import com.example.githubuserapp.database.UserFavorite
import com.example.githubuserapp.database.UserFavoriteDao

class ViewModelFavorite (application: Application): AndroidViewModel(application) {

    private var userDao: UserFavoriteDao?
    private var userDb : DatabaseUser?

    init {
        userDb = DatabaseUser.getDatabase(application)
        userDao = userDb?.userFavoriteDao()
    }

    fun getUserFavorite(): LiveData<List<UserFavorite>>?{
        return userDao?.getUserFavorite()
    }
}