package hr.davor_news.factory.di


import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hr.bagy94.android.base.localization.LocaleManager
import hr.bagy94.android.base.navigation.NavigationComponentController
import hr.bagy94.android.base.navigation.NavigationController
import hr.davor_news.android.common.error.AppErrorHandler
import hr.davor_news.android.common.router.AppRouter
import hr.davor_news.android.common.router.AppRouterImpl
import hr.davor_news.android.common.sharedpref.AppSharedPreference
import hr.davor_news.factory.error.AppErrorHandlerImpl
import hr.davor_news.factory.fragments.displaying_all_articles.DisplayingAllArticlesViewModel
import hr.davor_news.factory.fragments.displaying_articles_with_content.DisplayingArticlesContentViewModel
import hr.davor_news.factory.model.remote_source.INewsAPI
import hr.davor_news.factory.model.repositories.NewsRepository
import hr.davor_news.factory.network.getNewsApi
import hr.davor_news.factory.network.provideRetrofit
import hr.davor_news.factory.fragments.displaying_all_articles.AllArticlesScreenAdapter
import hr.davor_news.factory.fragments.displaying_articles_with_content.ArticlesWithContentScreenAdapter
import hr.davor_news.factory.fragments.displaying_error.DisplayingErrorDialog
import hr.davor_news.factory.sharedpref.AppSharedPreferencesImpl
import io.realm.RealmConfiguration
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single<Gson> { GsonBuilder().create() }
    single<AppSharedPreference> { AppSharedPreferencesImpl(get(), get()) }
    single<AppErrorHandler> { AppErrorHandlerImpl() }
    single { LocaleManager(get<AppSharedPreference>()) }
    factory<NavigationController> {  (fragment:Fragment)-> NavigationComponentController(fragment) }
    factory<AppRouter> { AppRouterImpl() }
}

val remoteModule = module {
    single<Retrofit> { provideRetrofit(get()) }
    single<INewsAPI> { getNewsApi(get()) }
}

val allArticlesModule = module {
    factory { AllArticlesScreenAdapter() }
    viewModel<DisplayingAllArticlesViewModel> { DisplayingAllArticlesViewModel(get(),get(),get()) }
}

val repositoryModule = module {
    single { NewsRepository(get(),get(), getRealmConfiguration()) }
}

val contentArticlesModule = module {
    factory { ArticlesWithContentScreenAdapter() }
    viewModel<DisplayingArticlesContentViewModel> { DisplayingArticlesContentViewModel(get(),get(),get()) }
}

val errorModule = module {
    single { DisplayingErrorDialog() }
}

fun getRealmConfiguration(): RealmConfiguration =
    RealmConfiguration.Builder()
        .name("news_realm7.realm")
        .schemaVersion(2)
        .build()


