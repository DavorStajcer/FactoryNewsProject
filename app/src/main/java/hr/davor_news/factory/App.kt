package hr.davor_news.factory

import hr.bagy94.android.base.app.BaseApp
import hr.bagy94.android.base.localization.LocaleManager
import hr.davor_news.factory.di.*
import io.realm.Realm
import org.koin.android.ext.android.get
import org.koin.core.module.Module

class App : BaseApp() {

    override val localeManager by lazy { get<LocaleManager>() }
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

    override fun provideKoinModules(): List<Module> {
        return listOf(
            appModule,
            remoteModule,
            allArticlesModule,
            repositoryModule,
            errorModule,
            contentArticlesModule
        )
    }

}