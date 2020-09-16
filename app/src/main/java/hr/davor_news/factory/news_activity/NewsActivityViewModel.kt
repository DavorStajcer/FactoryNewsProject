package hr.davor_news.factory.news_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.bagy94.android.base.error.APIError
import hr.bagy94.android.base.viewmodel.BaseViewModel
import hr.bagy94.android.base.viewmodel.RepositoryVM
import hr.bagy94.android.base.viewmodel.ScreenAdapter
import hr.davor_news.android.common.router.AppRouter
import hr.davor_news.factory.model.repositories.NewsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class NewsActivityViewModel(
    override val router: AppRouter,
    override val screenAdapter: NewsActivityScreenAdapter,
    override val repository: NewsRepository
) : BaseViewModel<AppRouter>() , RepositoryVM{

    init {
        checkIfThereAreArticlesInDatabase()
        makeNetworkCallEveryFiveMinutes()
    }

    fun changeFragments(){
        if(screenAdapter.changeFragmets.value == Fragments.ALL_ARTICLES)
            screenAdapter.changeFragmets.value = Fragments.ONE_ARTICLE
        else
            screenAdapter.changeFragmets.value = Fragments.ALL_ARTICLES
    }

    private fun makeNetworkCallEveryFiveMinutes(){
        Observable.interval(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(){
                if(screenAdapter.showErrorFragment.value!!)
                    return@subscribe
                makeNetworkCall()
            }

    }
    private fun makeNetworkCall(){
        repository.getArticlesFromNetwork()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                repository.saveOrUpdateLocalArticles(it.articles)
            }
            .subscribeToRepo()
    }
    private fun checkIfThereAreArticlesInDatabase(){
        repository.getArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if(!it.isNullOrEmpty())
                    makeNetworkCall()
            }
            .subscribeToRepo()
    }
    fun onDialogDismissed(){
        screenAdapter.showErrorFragment.value = false
    }
    override fun <T> onAPIError(error: APIError<T>) {
        super.onAPIError(error)
     //   screenAdapter.showErrorFragment.value = true
    }

}