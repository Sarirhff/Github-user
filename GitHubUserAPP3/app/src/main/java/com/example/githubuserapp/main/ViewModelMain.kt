package com.example.githubuserapp.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.model.User
import com.example.githubuserapp.model.UserResponse
import com.example.githubuserapp.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelMain : ViewModel(){

    val ListUsers = MutableLiveData<ArrayList<User>>()


    fun setSearchUsers (query: String){
        RetrofitClient.apiInstance
            .getSearchUser(query)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                )
                {
                    if(response.isSuccessful)
                        ListUsers.postValue(response.body()?.items)
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }

            })
    }

    fun getSearchUsers(): LiveData<ArrayList<User>> {

        return ListUsers
    }
}
