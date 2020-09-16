package hr.davor_news.factory.network

import android.content.Context
import hr.davor_news.android.common.sharedpref.AppSharedPreference
import hr.davor_news.factory.model.remote_source.INewsAPI
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


fun provideRetrofit(sharedPreferences: AppSharedPreference, context: Context) : Retrofit{
    return Retrofit.Builder()
        .baseUrl(sharedPreferences.apiURL)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttpClient(sharedPreferences,context))
        .build()
}

fun getNewsApi(retrofitInstance : Retrofit) : INewsAPI {
    return retrofitInstance.create(INewsAPI::class.java)
}