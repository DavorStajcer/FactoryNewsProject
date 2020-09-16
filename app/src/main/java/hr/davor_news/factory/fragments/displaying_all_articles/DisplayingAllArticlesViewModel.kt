package hr.davor_news.factory.fragments.displaying_all_articles

import hr.bagy94.android.base.viewmodel.BaseViewModel
import hr.bagy94.android.base.viewmodel.RepositoryVM
import hr.davor_news.android.common.router.AppRouter
import hr.davor_news.factory.model.local_database.Article
import hr.davor_news.factory.model.repositories.NewsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.RealmResults
import io.realm.RealmChangeListener


class DisplayingAllArticlesViewModel(
    override val router: AppRouter,
    override val screenAdapter: AllArticlesScreenAdapter,
    override val repository: NewsRepository
) : BaseViewModel<AppRouter>(), RepositoryVM, IOnArticleClickedListener {

    private lateinit var results : RealmResults<Article>
    private val tempList = mutableListOf<Article>()

    private val articlesRealmChangeListener by lazy {
        RealmChangeListener<RealmResults<Article>> { it ->
            if(results.isLoaded){
                for((index,article) in it.withIndex())
                    tempList[index] = article
                screenAdapter.listOfArticles.value = tempList
            }
        }
    }

    init {
        initializeRealmChangesListener()
    }

    private fun initializeRealmChangesListener(){
        repository.getArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                results = it
                for(article in results)
                    tempList.add(article)
                screenAdapter.listOfArticles.value = tempList
                results.addChangeListener(articlesRealmChangeListener)
            }
            .subscribeToRepo()
    }
    override fun changeFragments(positionOfArticleClicked: Int) {
        repository.saveOrUpdatePickedArticlePosition(positionOfArticleClicked)
        if (screenAdapter.changeFragments.value == null)
            screenAdapter.changeFragments.value = false
        else
            screenAdapter.changeFragments.value = !screenAdapter.changeFragments.value!!
    }

}



