package io.schrws.cherrysms.android.di

import io.schrws.cherrysms.android.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single { SharedPreferenceStorage(androidApplication()) }
    single { Cache(androidApplication().cacheDir, 10L * 1024 * 1024) }
    single { provideApi(get()) }
}

fun provideApi(cache: Cache): Api {
    val okHttp = OkHttpClient.Builder()
        .cache(cache)
        .retryOnConnectionFailure(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val builder = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")

            return@addInterceptor chain.proceed(builder.build())
        }
        .build()

    return Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_URL)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .build()
        .create(Api::class.java)
}