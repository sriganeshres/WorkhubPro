package com.work.workhubpro.di


import com.work.workhubpro.api.ChatApi
import com.work.workhubpro.api.UserApi
import com.work.workhubpro.api.WorkHubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val BASE_URL_1: String = "http://10.0.2.2:8000"
    private val BASE_URL_2: String = "http://10.0.2.2:8001"
    private val BASE_URL_3: String = "http://10.0.2.2:8002"

    @Singleton
    @Provides
    @Named("retrofit1")
    fun providesRetrofit1(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL_1)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideUserAPI(@Named("retrofit1") retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    @Named("retrofit2")
    fun provideRetrofit2(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWorkHubApi(@Named("retrofit2") retrofit: Retrofit): WorkHubApi {
        return retrofit.create(WorkHubApi::class.java)
    }
    @Provides
    @Singleton
    @Named("retrofit3")
    fun provideRetrofit3(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_3)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideChatApi(@Named("retrofit3") retrofit: Retrofit):ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

}