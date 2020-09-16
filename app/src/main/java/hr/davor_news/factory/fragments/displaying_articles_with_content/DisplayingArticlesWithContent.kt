package hr.davor_news.factory.fragments.displaying_articles_with_content

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hr.bagy94.android.base.fragment.BaseFragment
import hr.davor_news.factory.databinding.DisplayingScrollableArticlesLayoutBinding
import hr.davor_news.factory.R
import hr.davor_news.factory.adapters.screen_adapters.DisplayingScrollableArticlesRecyclerAdapter
import kotlinx.android.synthetic.main.displaying_scrollable_articles_layout.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class DisplayingArticlesWithContent() : BaseFragment<DisplayingArticlesWithContentViewModel,DisplayingScrollableArticlesLayoutBinding>() {
    override val layoutId: Int = R.layout.displaying_scrollable_articles_layout
    override val viewModel: DisplayingArticlesWithContentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerAdapter : DisplayingScrollableArticlesRecyclerAdapter by inject()
        setUpRecyclerView(recyclerAdapter)
        setUpViewModelObservers(recyclerAdapter)
    }

    private fun setUpViewModelObservers(recyclerAdapter : DisplayingScrollableArticlesRecyclerAdapter){
        viewModel.screenAdapter.listOfArticles.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.changeArticles(it)
        })
        viewModel.screenAdapter.scrollToPickedArticle.observe(viewLifecycleOwner, Observer {
            Log.i("click","Scrolling to article.")
            scrollableArticlesRecyclcerView.layoutManager?.scrollToPosition(it)
        })
    }
    private fun setUpRecyclerView(recyclerAdapter: DisplayingScrollableArticlesRecyclerAdapter){
        scrollableArticlesRecyclcerView.layoutManager = LinearLayoutManager(requireContext())
        scrollableArticlesRecyclcerView.adapter = recyclerAdapter
    }
}