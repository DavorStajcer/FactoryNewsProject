package hr.bagy94.android.common.router

import hr.bagy94.android.base.livedata.NavigationEvent
import hr.bagy94.android.base.navigation.NavigationTarget

class AppRouterImpl : AppRouter {
    override val backCallback = NavigationEvent<Unit>()
    override val navigationDirection = NavigationEvent<NavigationTarget>()
}