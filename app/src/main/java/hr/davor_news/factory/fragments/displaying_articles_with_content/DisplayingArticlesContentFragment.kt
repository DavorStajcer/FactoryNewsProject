package hr.davor_news.factory.fragments.displaying_articles_with_content


import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import hr.bagy94.android.base.fragment.BaseFragment
import hr.davor_news.factory.R
import hr.davor_news.factory.adapters.screen_adapters.DisplayingArticlesContentRecyclerAdapter
import hr.davor_news.factory.databinding.DisplayingArticlesContentLayoutBinding
import hr.davor_news.factory.fragments.displaying_error.DisplayingErrorDialog
import hr.davor_news.factory.fragments.displaying_error.IOnDialogDismissedListener
import kotlinx.android.synthetic.main.displaying_articles_content_layout.*
import org.koin.android.viewmodel.ext.android.viewModel

class DisplayingArticlesContentFragment() : BaseFragment<DisplayingArticlesContentViewModel,DisplayingArticlesContentLayoutBinding>(), IOnDialogDismissedListener {
    override val layoutId: Int = R.layout.displaying_articles_content_layout
    override val viewModel: DisplayingArticlesContentViewModel by viewModel()
    private val passedArguments : DisplayingArticlesContentFragmentArgs by navArgs()
    private var shouldScrollToPickedArticle : Boolean = true        //kada zovem u onResume() metodi " binding.articlesContentRecyclerView.scrollToPosition(passedArguments.positionOfPickedArticle)"
                                                                    //ne radi mi svaki puta, ako je zovem nakon sto se lista promjeni, radi, mozda ima veze sa notfyDataSetChanged ?

    override fun onResume() {
        super.onResume()
        shouldScrollToPickedArticle = true
        viewModel.compareTimeAndMakeNetworkCall()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerAdapter = DisplayingArticlesContentRecyclerAdapter(requireContext(), listOf())
        val networkErrorDialog = DisplayingErrorDialog().also {
            it.onDialogDismissedListener = this
        }
        setUpRecyclerView(recyclerAdapter)
        setUpViewModelObservers(recyclerAdapter,networkErrorDialog)
        requireActivity().setActionBar(articlesContentToolbar as Toolbar)
        (articlesContentToolbar as Toolbar).navigationIcon = ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_arrow_back_24,null)
        (articlesContentToolbar as Toolbar).setNavigationOnClickListener {
            viewModel.router.navigateUp()
        }
    }
    private fun setUpViewModelObservers(recyclerAdapter : DisplayingArticlesContentRecyclerAdapter,networkErrorDialog : DisplayingErrorDialog){
        viewModel.screenAdapter.listOfArticles.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.changeArticles(it)
            if(shouldScrollToPickedArticle)
                binding.articlesContentRecyclerView.scrollToPosition(passedArguments.positionOfPickedArticle).also {
                    shouldScrollToPickedArticle = false
                }
        })
        viewModel.screenAdapter.showNetworkErrorDialog.observe(viewLifecycleOwner, Observer {
            if(it)
                networkErrorDialog.getAlertDialog(requireContext()).show()
        })
    }
    private fun setUpRecyclerView(recyclerAdapter: DisplayingArticlesContentRecyclerAdapter){
        binding.articlesContentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.articlesContentRecyclerView.adapter = recyclerAdapter
    }
    override fun onDialogDismissed() {
        viewModel.onDialogDismissed()
    }
    override fun onPause() {
        super.onPause()
        viewModel.disposeOfCompareObservable()
    }
}