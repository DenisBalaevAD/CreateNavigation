package com.example.imageapi_retrafit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Since
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @GET("meow")
    fun getPage(): Call<MainPage>
}

data class MainPage(@SerializedName("file") @Expose val file:String)