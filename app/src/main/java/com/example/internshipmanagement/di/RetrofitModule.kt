package com.example.internshipmanagement.di

import com.example.internshipmanagement.data.remote.APIService
import com.example.internshipmanagement.util.SERVER_URL
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type

val apiModule = module {
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClientBuilder = OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
        }
        // Return an OkHttpClient
        return okHttpClientBuilder.build()
    }

    fun provideNullConverter(): Converter.Factory {
        return object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
                override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }
    }

    fun provideRetrofit(okHttpClient: OkHttpClient, nullConverter: Converter.Factory) = Retrofit.Builder()
        .baseUrl(SERVER_URL)
        .client(okHttpClient)
        .addConverterFactory(nullConverter)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    fun provideAPIModule(retrofit: Retrofit) = retrofit.create(APIService::class.java)

    single { provideOkHttpClient() }
    single { provideNullConverter() }
    single { provideRetrofit(get(), get()) }
    single { provideAPIModule(get()) }
}