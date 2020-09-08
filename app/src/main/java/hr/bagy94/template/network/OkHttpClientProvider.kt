package hr.bagy94.template.network

import hr.bagy94.android.common.sharedpref.AppSharedPreference
import hr.bagy94.template.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

fun provideOkHttpClient(sharedPreference: AppSharedPreference) : OkHttpClient {
    val builder = OkHttpClient.Builder()
    return builder.addInterceptor(HeaderInterceptor(sharedPreference))
        .apply {
            if (BuildConfig.DEBUG){
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(httpLoggingInterceptor)
            }
        }
        .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .build()
}

private const val CONNECTION_TIMEOUT = 30000L // miliseconds