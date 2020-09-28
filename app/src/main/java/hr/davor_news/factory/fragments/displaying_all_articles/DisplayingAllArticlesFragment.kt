package hr.davor_news.factory.fragments.displaying_all_articles

import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hr.bagy94.android.base.fragment.BaseFragment
import hr.davor_news.factory.R
import hr.davor_news.factory.adapters.screen_adapters.DisplayingAllArticlesRecyclerAdapter
import hr.davor_news.factory.databinding.DisplayingAllArticlesLayoutBinding
import hr.davor_news.factory.fragments.displaying_error.DisplayingErrorDialog
import hr.davor_news.factory.fragments.displaying_error.IOnDialogDismissedListener
import kotlinx.android.synthetic.main.displaying_all_articles_layout.*
import org.koin.android.viewmodel.ext.android.viewModel

class DisplayingAllArticlesFragment : BaseFragment<DisplayingAllArticlesViewModel,DisplayingAllArticlesLayoutBinding>(), IOnDialogDismissedListener {
    override val layoutId: Int = R.layout.displaying_all_articles_layout
    override val viewModel  by viewModel<DisplayingAllArticlesViewModel>()

    override fun onResume() {
        super.onResume()
        viewModel.compareTimeAndMakeNetworkCall()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerAdapter = DisplayingAllArticlesRecyclerAdapter(requireContext(),listOf())
        val networkErrorDialog = DisplayingErrorDialog().also {
            it.onDialogDismissedListener = this
        }
        recyclerAdapter.viewModelReference = viewModel
        setUpNewsRecyclerView(recyclerAdapter)
        setUpViewModelObservers(recyclerAdapter,networkErrorDialog)
        requireActivity().setActionBar(allArticlesToolbar as Toolbar)
    }
    private fun setUpViewModelObservers(recyclerAdapter : DisplayingAllArticlesRecyclerAdapter,networkErrorDialog : DisplayingErrorDialog){
        viewModel.screenAdapter.listOfArticles.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.changeArticles(it)
        })
        viewModel.screenAdapter.showNetworkErrorDialog.observe(viewLifecycleOwner, Observer {
            if(it)
                networkErrorDialog.getAlertDialog(requireContext()).show()
        })
        viewModel.router.setNavigationTargetEventObserver(viewLifecycleOwner,navigationController)
    }
    private fun setUpNewsRecyclerView(recyclerAdapter: DisplayingAllArticlesRecyclerAdapter) {
        binding.newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.newsRecyclerView.adapter = recyclerAdapter
    }
    override fun onDialogDismissed() {
        viewModel.onDialogDismissed()
    }

    override fun onPause() {
        super.onPause()
        viewModel.disposeOfCompareObservable()
    }
}
