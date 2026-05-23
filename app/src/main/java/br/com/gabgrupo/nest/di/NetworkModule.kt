package br.com.gabgrupo.nest.di

import br.com.gabgrupo.nest.BuildConfig
import br.com.gabgrupo.nest.data.remote.AuthApiService
import br.com.gabgrupo.nest.data.remote.DashboardApiService
import br.com.gabgrupo.nest.data.remote.GuidelineApiService
import br.com.gabgrupo.nest.data.remote.IdeaApiService
import br.com.gabgrupo.nest.data.remote.ProjectApiService
import br.com.gabgrupo.nest.data.local.TokenDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val EMULATOR_BASE_URL = "http://10.0.2.2:8080/"
    private const val DEVICE_BASE_URL = "http://<IP_DA_MAQUINA>:8080/"

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenDataStore: TokenDataStore): Interceptor {
        return Interceptor { chain ->
            val token = runBlocking { tokenDataStore.getToken() }
            val request = if (!token.isNullOrBlank()) {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                chain.request()
            }

            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(EMULATOR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGuidelineApi(retrofit: Retrofit): GuidelineApiService {
        return retrofit.create(GuidelineApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideIdeaApi(retrofit: Retrofit): IdeaApiService {
        return retrofit.create(IdeaApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProjectApi(retrofit: Retrofit): ProjectApiService {
        return retrofit.create(ProjectApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDashboardApi(retrofit: Retrofit): DashboardApiService {
        return retrofit.create(DashboardApiService::class.java)
    }
}