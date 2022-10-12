package com.example.pagenator_retrafit

import android.os.Environment
import com.mrwang.retrofitcacheinterceptor.RetrofitCacheInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Common {
    val retrofitServices:APIInterface
        get() = RetrofitClient()
            .getUser("https://reqres.in/api/")
            .create(APIInterface::class.java)
}

class RetrofitClient {

    fun getUser(baseUrl:String): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        /*val retrofitCacheInterceptor = RetrofitCacheInterceptor(context)

        val cacheDir = Environment.getExternalStorageDirectory()
        val cache = Cache(cacheDir, 20 * 1024 * 1024)

        val okHttpClient: OkHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(retrofitCacheInterceptor)
            .cache(cache)
            .build()*/

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}