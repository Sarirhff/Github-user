package com.example.githubuserapp.retrofit

import com.example.githubuserapp.model.UserResponse
import com.example.githubuserapp.model.DetailUserResponse
import com.example.githubuserapp.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET ("search/users" )
    @Headers ("Autorization: token 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")
    fun getSearchUser(
        @Query("q")query:String
    ): Call<UserResponse>

    @GET ("users/{username}")
    @Headers ("Autorization: token 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")
    fun getUserDetail(
        @Path("username")username:String
    ): Call<DetailUserResponse>

    @GET ("users/{username}/followers")
    @Headers ("Autorization: token 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")
    fun getUserFollowers(
        @Path("username")username:String
    ): Call<ArrayList<User>>

    @GET ("users/{username}/following")
    @Headers ("Autorization: token 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")
    fun getUserFollowing(
        @Path("username")username:String
    ): Call<ArrayList<User>>
}