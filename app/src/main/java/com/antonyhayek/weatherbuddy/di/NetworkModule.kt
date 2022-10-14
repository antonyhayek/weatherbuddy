package com.antonyhayek.weatherbuddy.di

import android.content.Context
import com.antonyhayek.weatherbuddy.data.networking.ApiService
import com.antonyhayek.weatherbuddy.data.networking.RequestInterceptor
import com.antonyhayek.weatherbuddy.data.networking.URLs
import com.antonyhayek.weatherbuddy.data.repository.CityRepositoryImpl
import com.antonyhayek.weatherbuddy.data.repository.SettingsRepositoryImpl
import com.antonyhayek.weatherbuddy.data.repository.WeatherRepositoryImpl
import com.antonyhayek.weatherbuddy.domain.database.dao.CityDao
import com.antonyhayek.weatherbuddy.domain.database.dao.FavoriteDao
import com.antonyhayek.weatherbuddy.domain.prefstore.PrefsStoreImpl
import com.antonyhayek.weatherbuddy.domain.repository.CityRepository
import com.antonyhayek.weatherbuddy.domain.repository.SettingsRepository
import com.antonyhayek.weatherbuddy.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideProductionUrl() = URLs.PRODUCTION_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(
        prefStoreImpl: PrefsStoreImpl,
        @ApplicationContext context: Context
    ) = run {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(RequestInterceptor(prefStoreImpl/*tokenWrapper, context, prefStoreImpl*/))
       //     .authenticator(tokenAuthenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    }

  /*  @Provides
    @Singleton
    fun provideWeatherReminder( @ApplicationContext context: Context): WeatherReminderManager {
        return WeatherReminderManager(context)
    }

*/
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, PRODUCTION_URL: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PRODUCTION_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(/*prefsStore: PrefsStoreImpl,*/ apiService: ApiService): WeatherRepository {
        return WeatherRepositoryImpl(/*prefsStore,*/ apiService)
    }

    @Provides
    @Singleton
    fun provideCityRepository(@ApplicationContext context: Context, cityDao: CityDao, favoriteDao: FavoriteDao) : CityRepository {
        return CityRepositoryImpl(context, cityDao, favoriteDao)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(prefsStoreImpl: PrefsStoreImpl) : SettingsRepository {
        return SettingsRepositoryImpl(prefsStoreImpl)
    }
}