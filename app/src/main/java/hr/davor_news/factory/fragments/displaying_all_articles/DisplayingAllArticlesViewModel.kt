package hr.davor_news.factory.fragments.displaying_all_articles

import hr.bagy94.android.base.error.APIError
import hr.bagy94.android.base.navigation.NavDirectionsWrapper
import hr.bagy94.android.base.viewmodel.BaseViewModel
import hr.bagy94.android.base.viewmodel.RepositoryVM
import hr.davor_news.android.common.router.AppRouter
import hr.davor_news.factory.model.repositories.NewsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class DisplayingAllArticlesViewModel(
    override val router: AppRouter,
    override val screenAdapter: AllArticlesScreenAdapter,
    override val repository: NewsRepository
) : BaseViewModel<AppRouter>(), RepositoryVM, IOnArticleClickedListener {

    init {
        makeNetworkCallEveryFiveMinutes()
        setUpDatabaseObservable()
    }

    private fun makeNetworkCallEveryFiveMinutes() {
        addDisposable(
            Observable.interval(5, TimeUnit.MINUTES)
                .doOnNext {
                    makeNetworkCall()
                }.doOnSubscribe {
                    makeNetworkCall()
                }.subscribe()
        )
    }
    private fun makeNetworkCall() {
        repository.getArticlesFromNetwork()
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeToRepo()
    }
    private fun setUpDatabaseObservable() {
        repository.getDatabaseObservable()
            .toObservable()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                screenAdapter.listOfArticles.value = it
            }
            .subscribeToRepo()
    }
    override fun <T> onAPIError(error: APIError<T>) {
        super.onAPIError(error)
        if (screenAdapter.showNetworkErrorDialog.value != true) //if you try to show error dialog while the other one is displaying you will get an error
            screenAdapter.showNetworkErrorDialog.value = true
    }
    fun onDialogDismissed() {
        screenAdapter.showNetworkErrorDialog.value = false
    }
    override fun onArticleClicked(positionOfArticleClicked: Int) {
        router.navigationDirection.value = NavDirectionsWrapper(
            navDirections = DisplayingAllArticlesFragmentDirections.openArticle(
                positionOfArticleClicked
            )
        )
    }
}



