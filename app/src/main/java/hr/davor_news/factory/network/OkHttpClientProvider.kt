package hr.davor_news.factory.network

import android.content.Context
import hr.bagy94.android.base.BuildConfig
import hr.davor_news.android.common.sharedpref.AppSharedPreference
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

fun provideOkHttpClient(sharedPreference: AppSharedPreference,context : Context) : OkHttpClient {
    val cacheSize = (4 *1024*1024).toLong()
    val newsCash = Cache(context.cacheDir, cacheSize)
    val builder = OkHttpClient.Builder()
    return builder.addInterceptor(HeaderInterceptor(sharedPreference))
        .apply {
            if (BuildConfig.DEBUG){
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(httpLoggingInterceptor)
            }
        }
        .cache(newsCash)
        .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .build()
}

private const val CONNECTION_TIMEOUT = 30000L // miliseconds