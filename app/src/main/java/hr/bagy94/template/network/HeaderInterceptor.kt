package hr.bagy94.template.network

import hr.bagy94.android.common.sharedpref.AppSharedPreference
import okhttp3.Interceptor
import okhttp3.Response

internal class HeaderInterceptor (private val sharedPreference: AppSharedPreference): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder()
            .addHeader(ACCEPT, JSON)
            .apply {
                sharedPreference.authToken?.let {
                    addHeader(AUTHORIZATION, "$BEARER $it")
                }
                val locale = sharedPreference.appLocale
                if (locale.isNotEmpty()) {
                    addHeader(LANGUAGE, locale)
                }
            }
            .build())

    }
}
private const val BEARER = "Bearer"
private const val AUTHORIZATION = "Authorization"
private const val ACCEPT = "Accept"
private const val JSON = "application/json"
private const val LANGUAGE = "Accept-Language"