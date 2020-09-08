package hr.bagy94.template.di

import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hr.bagy94.android.base.localization.LocaleManager
import hr.bagy94.android.base.navigation.NavigationComponentController
import hr.bagy94.android.base.navigation.NavigationController
import hr.bagy94.android.common.error.AppErrorHandler
import hr.bagy94.android.common.router.AppRouter
import hr.bagy94.android.common.router.AppRouterImpl
import hr.bagy94.android.common.sharedpref.AppSharedPreference
import hr.bagy94.template.error.AppErrorHandlerImpl
import hr.bagy94.template.sharedpref.AppSharedPreferencesImpl
import org.koin.dsl.module

val appModule = module {
    single<Gson> { GsonBuilder().create() }
    single<AppSharedPreference> { AppSharedPreferencesImpl(get(), get()) }
    single<AppErrorHandler> { AppErrorHandlerImpl() }
    single { LocaleManager(get<AppSharedPreference>()) }
    factory<NavigationController> {  (fragment:Fragment)-> NavigationComponentController(fragment) }
    factory<AppRouter> { AppRouterImpl() }
}