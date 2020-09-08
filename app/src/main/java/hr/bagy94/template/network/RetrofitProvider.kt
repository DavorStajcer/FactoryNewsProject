package hr.bagy94.template.network

import hr.bagy94.android.common.sharedpref.AppSharedPreference
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


fun provideRetrofit(sharedPreferences:AppSharedPreference) : Retrofit{
    return Retrofit.Builder()
        .baseUrl(sharedPreferences.apiURL)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttpClient(sharedPreferences))
        .build()
}