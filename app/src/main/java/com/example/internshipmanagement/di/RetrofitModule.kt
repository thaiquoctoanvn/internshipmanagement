package com.example.internshipmanagement.di

import com.example.internshipmanagement.data.remote.APIService
import com.example.internshipmanagement.util.SERVER_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val apiModule = module {
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClientBuilder = OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
        }
        // Return an OkHttpClient
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(SERVER_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    fun provideAPIModule(retrofit: Retrofit) = retrofit.create(APIService::class.java)

    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideAPIModule(get()) }
}