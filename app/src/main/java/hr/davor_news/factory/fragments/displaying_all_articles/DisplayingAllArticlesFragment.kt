package hr.davor_news.factory.fragments.displaying_all_articles

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hr.bagy94.android.base.fragment.BaseFragment
import hr.davor_news.factory.R
import hr.davor_news.factory.adapters.screen_adapters.DisplayingAllArticlesRecyclerAdapter
import hr.davor_news.factory.databinding.DisplayingAllArticlesLayoutBinding
import hr.davor_news.factory.news_activity.IChangeFragmentListener
import kotlinx.android.synthetic.main.displaying_all_articles_layout.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class DisplayingAllArticlesFragment : BaseFragment<DisplayingAllArticlesViewModel,DisplayingAllArticlesLayoutBinding>() {
    override val layoutId: Int = R.layout.displaying_all_articles_layout
    override val viewModel  by viewModel<DisplayingAllArticlesViewModel>()
    var changeFragmentListener : IChangeFragmentListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerAdapter : DisplayingAllArticlesRecyclerAdapter by inject()
        recyclerAdapter.viewModelReference = viewModel
        setUpNewsRecyclerView(recyclerAdapter)
        setListOfArticlesObserver(recyclerAdapter)

    }
    private fun setListOfArticlesObserver(recyclerAdapter : DisplayingAllArticlesRecyclerAdapter){
        viewModel.screenAdapter.listOfArticles.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.changeArticles(it)
        })
        viewModel.screenAdapter.changeFragments.observe(viewLifecycleOwner, Observer {
                changeFragmentListener!!.changeFragments()
        })
    }
    private fun setUpNewsRecyclerView(recyclerAdapter: DisplayingAllArticlesRecyclerAdapter) {
        newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsRecyclerView.adapter = recyclerAdapter
    }

}
