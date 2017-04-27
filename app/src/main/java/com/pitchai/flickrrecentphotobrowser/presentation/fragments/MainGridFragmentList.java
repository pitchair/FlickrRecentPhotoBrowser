package com.pitchai.flickrrecentphotobrowser.presentation.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pitchai.flickrrecentphotobrowser.AndroidApplication;
import com.pitchai.flickrrecentphotobrowser.R;
import com.pitchai.flickrrecentphotobrowser.RecyclerItemClickListener;
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo;
import com.pitchai.flickrrecentphotobrowser.presentation.adapter.RecyclerPhotoAdapter;
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules
        .MainGridListFragmentModule;
import com.pitchai.flickrrecentphotobrowser.presentation.presenter.GridPhotoListPresenterImp;
import com.pitchai.flickrrecentphotobrowser.presentation.view.GridPhotoListView;
import com.pitchai.flickrrecentphotobrowser.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainGridFragmentList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainGridFragmentList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainGridFragmentList extends BaseFragment implements GridPhotoListView {

    private static final String TAG = MainGridFragmentList.class.getSimpleName();
    public static int mColWidth;
    private OnFragmentInteractionListener mListener;
    SwipeRefreshLayout mSwipeRefresh;
    protected MenuItem mRefreshMenuItem;
    private RecyclerView mRecyclerView;
    private int scrollPreviousTotal = 0;
    private boolean loading = true;
    @Inject
    public RecyclerPhotoAdapter mRecyclerPhotoAdapter;
    @Inject
    public GridPhotoListPresenterImp gridPhotoListPresenterImp;
    ArrayList<Photo> mPhotoList;
    int mCurrentPageNumber;
    ProgressBar mProgressBar;
    //UsersAdapter usersAdapter;

    public MainGridFragmentList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainGridFragmentList.
     */
    public static MainGridFragmentList newInstance() {
        MainGridFragmentList fragment = new MainGridFragmentList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mPhotoList = new ArrayList<>();
        AndroidApplication.getAndroidApplication(this.getActivity())
                .getApplicationComponent()
                .plus(new MainGridListFragmentModule(this))
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main_grid, container, false);
        initializeView(view);
        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onDestroy() {
        super.onDestroy();
        gridPhotoListPresenterImp.destroy();
    }

    /**
     * Initialize Views
     */
    private void initializeView(View view) {

        mProgressBar = (ProgressBar) view.findViewById(R.id.activity_progressbar);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        // usersAdapter = new UsersAdapter(getActivity().getApplicationContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.photo_recycler_view);
        /*mRecyclerView.setLayoutManager(new UsersLayoutManager(getActivity()
        .getApplicationContext()));
        mRecyclerView.setAdapter(usersAdapter);*/
        mRecyclerView.setHasFixedSize(true);
        mColWidth = Utils.calculateWidth(getActivity());
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity()
                .getApplicationContext(), Utils.calculateWidthScale(getActivity()));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerPhotoAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity()
                .getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "position:" + position);
                mListener.onPhotoSelect(mPhotoList, position);
            }
        }));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int x, int y) {
                super.onScrolled(recyclerView, x, y);
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();
                if (loading) {
                    if (totalItemCount > scrollPreviousTotal) {
                        loading = false;
                        scrollPreviousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem +
                        visibleItemCount)) {
                    //Load more page
                    loadMore();
                    loading = true;
                }
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridPhotoListPresenterImp.setGridPhotoListView(this);
        if (mPhotoList.size() == 0) {
            gridPhotoListPresenterImp.execute(1);
        } else {
            mRecyclerPhotoAdapter.updateList(mPhotoList);
        }
    }

    @Override
    public void renderPhotoList(List<Photo> photoList, int currentPage) {
        if (mSwipeRefresh.isRefreshing()) {
            stopRefreshProgress();
        }
        mCurrentPageNumber = currentPage;
        mPhotoList.addAll(photoList);
        //Update the adapter
        mRecyclerPhotoAdapter.updateList(photoList);
    }


    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context context() {
        return null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onPhotoSelect(ArrayList<Photo> photoList, int position);
    }

    private void loadMore() {
        gridPhotoListPresenterImp.execute(mCurrentPageNumber + 1);
    }


    private void refresh() {
        scrollPreviousTotal = 0;
        mPhotoList.clear();
        mRecyclerPhotoAdapter.clear();
        gridPhotoListPresenterImp.execute(1);
    }

    /**
     * Remove the refresh views on complete
     */
    private void stopRefreshProgress() {
        if (mRefreshMenuItem != null) {
            mRecyclerView.getLayoutManager().scrollToPosition(0);
            mRefreshMenuItem.setActionView(null);
        }
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        mRefreshMenuItem = menu.findItem(R.id.refresh);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                mRefreshMenuItem.setActionView(R.layout.actionbar_progress);
                refresh();
                return true;
        }
        return false;
    }
}
