package com.example.codeforces.api

import com.example.codeforces.models.Contests
import com.example.codeforces.models.userInfo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface codeforcesApi {
    @GET("/api/user.info")
    fun getUser(@Query("handles") user_name: String): Call<userInfo>

    /*
    //    @GET("/api/user.rating")
//    fun getUserRatingChanges(@Query("handle") user_name: String): Call<contestDetails>
//
//    @GET("/api/user.status")
//    fun getUserSubmissions(@Query("handle") user_name: String): Call<userSubmission>
     */
    @GET("/api/contest.list")
    fun getContestList(): Call<Contests>
//    https://codeforces.com/api/contest.list?gym=true

    companion object {
        var BASE_URL = "https://codeforces.com/"

        fun create(): codeforcesApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(codeforcesApi::class.java)
        }
    }
}