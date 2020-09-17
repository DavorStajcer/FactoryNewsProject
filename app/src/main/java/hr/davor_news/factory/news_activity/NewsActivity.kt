package hr.davor_news.factory.news_activity

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import hr.bagy94.android.base.activity.BaseActivity
import hr.bagy94.android.base.fragment.BaseFragment
import hr.davor_news.factory.R
import hr.davor_news.factory.fragments.displaying_all_articles.DisplayingAllArticlesFragment
import hr.davor_news.factory.fragments.displaying_articles_with_content.DisplayingArticlesContentFragment
import hr.davor_news.factory.fragments.displaying_error.DisplayingErrorDialog
import hr.davor_news.factory.fragments.displaying_error.IOnDialogDismissedListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel



class NewsActivity() : BaseActivity(), IChangeFragmentListener , IOnDialogDismissedListener{
    override val layoutId: Int = R.layout.news_activity_layout
    private val viewModel : NewsActivityViewModel by viewModel()
    private val displayingAllArticlesFragment : DisplayingAllArticlesFragment by inject()
    private val errorDialog : DisplayingErrorDialog by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayingScrollableArticlesFragment : DisplayingArticlesContentFragment by inject()
        setUpListeners()
        setUpViewModelObservers(displayingScrollableArticlesFragment)
        errorDialog.onDialogDismissedListener = this
    }
    private fun setUpViewModelObservers(displayingScrollableArticlesFragment : DisplayingArticlesContentFragment){
        viewModel.screenAdapter.changeFragmets.observe(this, Observer {
            when(it!!){
                Fragments.ALL_ARTICLES -> changeFragmentTo(displayingAllArticlesFragment)
                Fragments.ONE_ARTICLE -> changeFragmentTo(displayingScrollableArticlesFragment)
            }
        })
        viewModel.screenAdapter.showErrorFragment.observe(this, Observer {
            if(it)
                errorDialog.getAlertDialog(this).show()
        })
    }
    private fun setUpListeners(){
        displayingAllArticlesFragment.changeFragmentListener = this
    }
    private fun changeFragmentTo(fragment : BaseFragment<*,*>) {
        supportFragmentManager.beginTransaction().apply {
            if (fragment is DisplayingArticlesContentFragment)
                addToBackStack("Articles")
            replace(R.id.mainFragmentContainerView, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            commit()
        }
    }
    override fun changeFragments(){
       viewModel.changeFragments()
    }
    override fun onDialogDismissed() {
        viewModel.onDialogDismissed()
    }


}

