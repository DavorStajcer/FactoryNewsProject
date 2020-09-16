package hr.davor_news.factory.fragments.displaying_articles_with_content

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.bagy94.android.base.viewmodel.BaseViewModel
import hr.bagy94.android.base.viewmodel.RepositoryVM
import hr.bagy94.android.base.viewmodel.ScreenAdapter
import hr.davor_news.android.common.router.AppRouter
import hr.davor_news.factory.model.local_database.Article
import hr.davor_news.factory.model.local_database.PositionOfArticleOpened
import hr.davor_news.factory.model.repositories.NewsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.RealmChangeListener
import io.realm.RealmResults

class DisplayingArticlesWithContentViewModel(
    override val router: AppRouter,
    override val screenAdapter: ArticlesWithContentScreenAdapter,
    override val repository: NewsRepository
) : BaseViewModel<AppRouter>(), RepositoryVM {


    private lateinit var articleResults : RealmResults<Article>
    private val tempList = mutableListOf<Article>()
    private lateinit var positionResult : RealmResults<PositionOfArticleOpened>

    private val realmArticlesChangedListener by lazy {
        RealmChangeListener<RealmResults<Article>> { it ->
            if(articleResults.isLoaded){
                for((index,article) in it.withIndex())
                    tempList[index] = article
                screenAdapter.listOfArticles.value = tempList
            }
        }
    }
    private val positionChangeListener by lazy {
        RealmChangeListener<RealmResults<PositionOfArticleOpened>> {
            if(positionResult.isLoaded){
                screenAdapter.scrollToPickedArticle.value = it.first()?.positionId
            }
        }
    }

    init {
        initializeRealmArticlesChangedListener()
        initializeRealmPositionOfArticleOpenedListener()
    }

     private fun initializeRealmArticlesChangedListener(){
       repository.getArticles().
           subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .doOnNext {
                articleResults = it
               for((index,article) in articleResults.withIndex())
                   tempList.add(article)
               screenAdapter.listOfArticles.value = tempList
               articleResults.addChangeListener(realmArticlesChangedListener)
            }.subscribeToRepo()
    }
    private fun initializeRealmPositionOfArticleOpenedListener(){
        repository.getPositionOfArticleOpened().
            subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                positionResult = it
                screenAdapter.scrollToPickedArticle.value = positionResult.first()?.positionOfArticlePicked
                positionResult.addChangeListener(positionChangeListener)
            }
            .subscribeToRepo()
    }

}