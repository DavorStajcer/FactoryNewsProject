package hr.davor_news.factory.fragments.displaying_all_articles

import android.util.Log
import hr.bagy94.android.base.error.APIError
import hr.bagy94.android.base.navigation.NavDirectionsWrapper
import hr.bagy94.android.base.viewmodel.BaseViewModel
import hr.bagy94.android.base.viewmodel.RepositoryVM
import hr.davor_news.android.common.router.AppRouter
import hr.davor_news.factory.model.repositories.NewsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class DisplayingAllArticlesViewModel(
    override val router: AppRouter,
    override val screenAdapter: AllArticlesScreenAdapter,
    override val repository: NewsRepository
) : BaseViewModel<AppRouter>(), RepositoryVM, IOnArticleClickedListener {

    init {
        setUpDatabaseObserver()
    }

    fun checkShouldNetworkCallBeMade(){
        if(repository.shouldNetworkCallBeMade())
            makeNetworkCall()
    }
    private fun makeNetworkCall() {
        repository.getArticlesFromNetwork()
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeToRepo()
    }
    private fun setUpDatabaseObserver() {
     repository.getDatabaseObservable()
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.i("checkShould","TOn next -> $it")
                screenAdapter.listOfArticles.value = it
            }.doOnError {
             Log.i("checkShould","Error while fetchin data from database.")
         }.subscribeToRepo()
    }
    override fun <T> onAPIError(error: APIError<T>) {
        super.onAPIError(error)
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



