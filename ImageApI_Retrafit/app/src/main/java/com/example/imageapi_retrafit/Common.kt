package com.example.imageapi_retrafit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Common {
    val retrofitServices:APIInterface
        get() = RetrofitClient().getUser("https://aws.random.cat/").create(APIInterface::class.java)
}

class RetrofitClient {

    private var retrofit:  Retrofit?=null

    fun getUser(baseUrl:String): Retrofit {

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit!!

    }
}