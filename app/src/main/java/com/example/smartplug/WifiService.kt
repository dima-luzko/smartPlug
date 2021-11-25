package com.example.smartplug

import android.content.Context
import android.util.Log
import com.example.smartplug.databinding.WifiActivityBinding
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class WifiService(context: Context) {

    companion object {
        private const val RESULT_TAG = "request"
    }

    var url = "http://192.168.1.110:80/"
    private val gson = GsonBuilder().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(getHttpClient())
        .build()
        .create(Api::class.java)

    suspend fun getResponse(ledId: String): ResponseApi? {
        val result = retrofit.get(ledId)
        return if (result.isSuccessful) {
            val resultSuccess = result.body()
            Log.d(RESULT_TAG, "request success: $resultSuccess, url:$url")
            resultSuccess
        } else {
            Log.d(RESULT_TAG, "request not success")
            null
        }
    }

    private fun getHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(1L, TimeUnit.MINUTES)
        return builder.build()
    }

    interface Api {
        @GET("{ledId}")
        suspend fun get(@Path("ledId") ledId: String): Response<ResponseApi>
    }

    data class ResponseApi(
       // val response: String,
        @SerializedName("led_1")
        val led1: Boolean,
        @SerializedName("led_2")
        val led2: Boolean,
        val power: Int
    )

}