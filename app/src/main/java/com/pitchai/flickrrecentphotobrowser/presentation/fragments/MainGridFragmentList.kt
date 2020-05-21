package com.pitchai.flickrrecentphotobrowser.presentation.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import com.pitchai.flickrrecentphotobrowser.AndroidApplication
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.RecyclerItemClickListener
import com.pitchai.flickrrecentphotobrowser.RecyclerItemClickListener.OnItemClickListener
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo
import com.pitchai.flickrrecentphotobrowser.presentation.adapter.RecyclerPhotoAdapter
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules.MainGridListFragmentModule
import com.pitchai.flickrrecentphotobrowser.presentation.presenter.GridPhotoListPresenterImp
import com.pitchai.flickrrecentphotobrowser.presentation.view.GridPhotoListView
import com.pitchai.flickrrecentphotobrowser.utils.Utils
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainGridFragmentList.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainGridFragmentList.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainGridFragmentList  //UsersAdapter usersAdapter;
    : BaseFragment(), GridPhotoListView {
    private var mListener: OnFragmentInteractionListener? = null
    var mSwipeRefresh: SwipeRefreshLayout? = null
    protected var mRefreshMenuItem: MenuItem? = null
    private var mRecyclerView: RecyclerView? = null
    private var scrollPreviousTotal = 0
    private var loading = true

    @Inject
    lateinit var mRecyclerPhotoAdapter: RecyclerPhotoAdapter

    @Inject
    lateinit var gridPhotoListPresenterImp: GridPhotoListPresenterImp
    var mPhotoList: ArrayList<Photo?>? = null
    var mCurrentPageNumber = 0
    var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
        mPhotoList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_main_grid, container, false)
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
            throw RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener")
        }
    }

//    override fun onAttach(context: Activity) {
//        super.onAttach(context)
//        mListener = if (context is OnFragmentInteractionListener) {
//            context
//        } else {
//            throw RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener")
//        }
//    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        gridPhotoListPresenterImp.destroy()
    }

    /**
     * Initialize Views
     */
    private fun initializeView(view: View) {
        mProgressBar = view.findViewById<View>(R.id.activity_progressbar) as ProgressBar
        mSwipeRefresh = view.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        // usersAdapter = new UsersAdapter(getActivity().getApplicationContext());
        mRecyclerView = view.findViewById<View>(R.id.photo_recycler_view) as RecyclerView
        /*mRecyclerView.setLayoutManager(new UsersLayoutManager(getActivity()
        .getApplicationContext()));
        mRecyclerView.setAdapter(usersAdapter);*/mRecyclerView!!.setHasFixedSize(true)
        mColWidth = Utils.calculateWidth(activity)
        val layoutManager = GridLayoutManager(activity
                .applicationContext, Utils.calculateWidthScale(activity))
        mRecyclerView?.layoutManager = layoutManager
        mRecyclerView?.adapter = mRecyclerPhotoAdapter
        mRecyclerView?.addOnItemTouchListener(RecyclerItemClickListener(activity
                .applicationContext, object : OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                Log.d(TAG, "position:$position")
                mListener?.onPhotoSelect(mPhotoList, position)
            }
        }))
        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, x: Int, y: Int) {
                super.onScrolled(recyclerView, x, y)
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = recyclerView.childCount
                if (loading) {
                    if (totalItemCount > scrollPreviousTotal) {
                        loading = false
                        scrollPreviousTotal = totalItemCount
                    }
                }
                if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem +
                        visibleItemCount) {
                    //Load more page
                    loadMore()
                    loading = true
                }
            }
        })
        mSwipeRefresh!!.setOnRefreshListener { refresh() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridPhotoListPresenterImp.setGridPhotoListView(this)
        if (mPhotoList!!.size == 0) {
            gridPhotoListPresenterImp.execute(1)
        } else {
            mRecyclerPhotoAdapter.updateList(mPhotoList)
        }
    }

    override fun renderPhotoList(photoList: List<Photo?>?, currentPage: Int) {
        if (mSwipeRefresh?.isRefreshing == true) {
            stopRefreshProgress()
        }
        mCurrentPageNumber = currentPage
        photoList?.takeUnless { it.isEmpty() }?.apply {
            mPhotoList?.addAll(this)
            //Update the adapter
            mRecyclerPhotoAdapter.updateList(photoList)
        }
    }

    override fun showLoading() {
        mProgressBar!!.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        mProgressBar!!.visibility = View.GONE
    }

    override fun showError(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun context(): Context? {
        return null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        fun onPhotoSelect(photoList: ArrayList<Photo?>?, position: Int)
    }

    private fun loadMore() {
        gridPhotoListPresenterImp.execute(mCurrentPageNumber + 1)
    }

    private fun refresh() {
        scrollPreviousTotal = 0
        mPhotoList?.clear()
        mRecyclerPhotoAdapter.clear()
        gridPhotoListPresenterImp.execute(1)
    }

    /**
     * Remove the refresh views on complete
     */
    private fun stopRefreshProgress() {
        if (mRefreshMenuItem != null) {
            mRecyclerView?.layoutManager?.scrollToPosition(0)
            mRefreshMenuItem?.setActionView(null)
        }
        if (mSwipeRefresh?.isRefreshing == true) {
            mSwipeRefresh?.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        mRefreshMenuItem = menu.findItem(R.id.refresh)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                mRefreshMenuItem!!.setActionView(R.layout.actionbar_progress)
                refresh()
                return true
            }
        }
        return false
    }

    companion object {
        private val TAG = MainGridFragmentList::class.java.simpleName
        var mColWidth = 0

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MainGridFragmentList.
         */
        fun newInstance(): MainGridFragmentList {
            val fragment = MainGridFragmentList()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}