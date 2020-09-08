package hr.bagy94.template

import hr.bagy94.android.base.app.BaseApp
import hr.bagy94.android.base.localization.LocaleManager
import hr.bagy94.template.di.appModule
import org.koin.android.ext.android.get
import org.koin.core.module.Module

class App : BaseApp() {
    override val localeManager by lazy { get<LocaleManager>() }

    override fun provideKoinModules(): List<Module> {
        return listOf(
            appModule
        )
    }
}