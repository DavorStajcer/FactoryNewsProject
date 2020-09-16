package hr.davor_news.factory.fragments.displaying_articles_with_content

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hr.bagy94.android.base.fragment.BaseFragment
import hr.davor_news.factory.R
import hr.davor_news.factory.adapters.screen_adapters.DisplayingArticlesContentRecyclerAdapter
import hr.davor_news.factory.databinding.DisplayingArticlesContentLayoutBinding
import kotlinx.android.synthetic.main.displaying_articles_content_layout.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class DisplayingArticlesContentFragment() : BaseFragment<DisplayingArticlesContentViewModel,DisplayingArticlesContentLayoutBinding>() {
    override val layoutId: Int = R.layout.displaying_articles_content_layout
    override val viewModel: DisplayingArticlesContentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerAdapter : DisplayingArticlesContentRecyclerAdapter by inject()
        setUpRecyclerView(recyclerAdapter)
        setUpViewModelObservers(recyclerAdapter)
    }

    private fun setUpViewModelObservers(recyclerAdapter : DisplayingArticlesContentRecyclerAdapter){
        viewModel.screenAdapter.listOfArticles.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.changeArticles(it)
        })
        viewModel.screenAdapter.scrollToPickedArticle.observe(viewLifecycleOwner, Observer {
            scrollableArticlesRecyclcerView.layoutManager?.scrollToPosition(it)
        })
    }
    private fun setUpRecyclerView(recyclerAdapter: DisplayingArticlesContentRecyclerAdapter){
        scrollableArticlesRecyclcerView.layoutManager = LinearLayoutManager(requireContext())
        scrollableArticlesRecyclcerView.adapter = recyclerAdapter
    }
}