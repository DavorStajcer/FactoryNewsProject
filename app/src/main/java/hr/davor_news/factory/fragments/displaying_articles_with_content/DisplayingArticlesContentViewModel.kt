package hr.davor_news.factory.fragments.displaying_articles_with_content

import hr.bagy94.android.base.error.APIError
import hr.bagy94.android.base.viewmodel.BaseViewModel
import hr.bagy94.android.base.viewmodel.RepositoryVM
import hr.davor_news.android.common.router.AppRouter
import hr.davor_news.factory.model.repositories.NewsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class DisplayingArticlesContentViewModel(
    override val router: AppRouter,
    override val screenAdapter: ArticlesWithContentScreenAdapter,
    override val repository: NewsRepository
) : BaseViewModel<AppRouter>(), RepositoryVM {

    init {
        makeNetworkCallEveryFiveMinutes()
        setUpDatabaseObservable()
    }
    private fun makeNetworkCall() {
        repository.getArticlesFromNetwork()
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeToRepo()
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

    private fun setUpDatabaseObservable(){
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
        if(screenAdapter.showNetworkErrorDialog.value != true) //if you try to show error dialog while the other one is displaying you will get an error
            screenAdapter.showNetworkErrorDialog.value = true
    }
    fun onDialogDismissed(){
        screenAdapter.showNetworkErrorDialog.value = false
    }
}