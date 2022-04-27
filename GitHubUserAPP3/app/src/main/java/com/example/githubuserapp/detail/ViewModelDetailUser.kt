package com.example.githubuserapp.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuserapp.database.DatabaseUser
import com.example.githubuserapp.database.UserFavorite
import com.example.githubuserapp.database.UserFavoriteDao
import com.example.githubuserapp.model.DetailUserResponse
import com.example.githubuserapp.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class ViewModelDetailUser (application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DetailUserResponse>()

    private var userDao: UserFavoriteDao?
    private var userDb : DatabaseUser?

    init {
        userDb = DatabaseUser.getDatabase(application)
        userDao = userDb?.userFavoriteDao()
    }

    fun setUserDetail(username: String) {
        RetrofitClient.apiInstance
            .getUserDetail(username)
            .enqueue(object : retrofit2.Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }

            })
    }

    fun getUserDetail(): LiveData<DetailUserResponse> {
        return user
    }
    fun addToFavorite(username: String, id: Int, avatar_url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserFavorite(
                username,
                id,
                avatar_url
            )
            userDao?.addToFavorite(user)
        }
    }
    fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.resolveFromFavorite(id)
        }
    }
}
