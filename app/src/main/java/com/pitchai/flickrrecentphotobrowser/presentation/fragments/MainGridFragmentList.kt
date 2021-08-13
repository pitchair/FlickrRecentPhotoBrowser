package com.pitchai.flickrrecentphotobrowser.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.pitchai.flickrrecentphotobrowser.AndroidApplication
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.databinding.FragmentMainGridBinding
import com.pitchai.flickrrecentphotobrowser.di.MainGridListFragmentModule
import com.pitchai.flickrrecentphotobrowser.presentation.ViewModel.PhotoListViewModel
import com.pitchai.flickrrecentphotobrowser.presentation.adapter.FlickrLoadStateAdapter
import com.pitchai.flickrrecentphotobrowser.presentation.adapter.PhotoClickListener
import com.pitchai.flickrrecentphotobrowser.presentation.adapter.RecyclerPhotoListAdapter
import com.pitchai.flickrrecentphotobrowser.presentation.view.LoadDataView
import com.pitchai.flickrrecentphotobrowser.utils.Utils
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainGridFragmentList.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainGridFragmentList.newInstance] factory method to
 * create an instance of this fragment.
 */
@InternalCoroutinesApi
class MainGridFragmentList : BaseFragment(), LoadDataView, PhotoClickListener {
    @Inject
    lateinit var mRecyclerPhotoAdapter: RecyclerPhotoListAdapter

    @Inject
    lateinit var photoListViewModel: PhotoListViewModel

    private var mListener: OnFragmentInteractionListener? = null
    protected var mRefreshMenuItem: MenuItem? = null
    private lateinit var binding: FragmentMainGridBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainGridBinding.inflate(inflater, container, false)
        val view = binding.root
        setHasOptionsMenu(true)
        initializeView(view)
        // Inflate the layout for this fragment
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidApplication.getAndroidApplication(this.activity).applicationComponent
            ?.plus(MainGridListFragmentModule(this))
            ?.inject(this)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * Initialize Views
     */
    private fun initializeView(view: View) {

        binding.photoRecyclerView.setHasFixedSize(true)
        val mColWidth = Utils.calculateWidth(requireActivity())
        mRecyclerPhotoAdapter.columnWidth = mColWidth
        val layoutManager = GridLayoutManager(
            activity
                ?.applicationContext, Utils.calculateWidthScale(requireActivity())
        )
        binding.photoRecyclerView.layoutManager = layoutManager
        binding.photoRecyclerView.adapter = mRecyclerPhotoAdapter.withLoadStateHeaderAndFooter(
            header = FlickrLoadStateAdapter { mRecyclerPhotoAdapter.retry() },
            footer = FlickrLoadStateAdapter { mRecyclerPhotoAdapter.retry() }
        )

        mRecyclerPhotoAdapter.addLoadStateListener { loadState ->

            // show empty list
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && mRecyclerPhotoAdapter.itemCount == 0
            showEmptyList(isListEmpty)

            // Only show the list if refresh succeeds.
            binding.photoRecyclerView.isVisible =
                loadState.mediator?.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            //binding.retryButton.isVisible = loadState.mediator?.refresh is LoadState.Error
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                showError("\uD83D\uDE28 Wooops ${it.error}")
            }
        }
        // Scroll to top when the list is refreshed from network.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                mRecyclerPhotoAdapter.loadStateFlow
//                    // Only emit when REFRESH LoadState for RemoteMediator changes.
//                    .distinctUntilChangedBy {
//                        it.refresh
//                    }
//                    // Only react to cases where Remote REFRESH completes i.e., NotLoading.
//                    .filter {
//                        it.refresh is LoadState.NotLoading
//                    }
//                    .collect {
//                        mRecyclerView?.scrollToPosition(0)
//                    }
                photoListViewModel.getPhotoList().collectLatest {
                    stopRefreshProgress()
                    mRecyclerPhotoAdapter.submitData(it)
                }
            }
        }
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showError(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.photoRecyclerView.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.photoRecyclerView.visibility = View.VISIBLE
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     */
    interface OnFragmentInteractionListener {
        fun onPhotoSelect(position: Int)
    }

    private fun refresh() {
        mRecyclerPhotoAdapter.refresh()
    }

    /**
     * Remove the refresh views on complete
     */
    private fun stopRefreshProgress() {
        if (mRefreshMenuItem != null) {
            //mRecyclerView?.layoutManager?.scrollToPosition(0)
            mRefreshMenuItem?.setActionView(null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        mRefreshMenuItem = menu.findItem(R.id.refresh)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                mRefreshMenuItem?.setActionView(R.layout.actionbar_progress)
                refresh()
                return true
            }
        }
        return false
    }

    companion object {
        private val TAG = MainGridFragmentList::class.java.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MainGridFragmentList.
         */
        fun newInstance(): Fragment {
            val fragment = MainGridFragmentList()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int) {
        mListener?.onPhotoSelect(position)
    }
}