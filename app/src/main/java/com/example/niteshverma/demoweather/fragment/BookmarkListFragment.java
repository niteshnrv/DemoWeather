package com.example.niteshverma.demoweather.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.niteshverma.demoweather.DemoApplication;
import com.example.niteshverma.demoweather.R;
import com.example.niteshverma.demoweather.Utility.PreferenceManager;
import com.example.niteshverma.demoweather.activity.DashboardActivity;
import com.example.niteshverma.demoweather.adapter.BookmarkListAdapter;
import com.example.niteshverma.demoweather.database.DBHelper;
import com.example.niteshverma.demoweather.dialog.AddLocationDialogFragment;
import com.example.niteshverma.demoweather.dialog.HelpDialogFragment;
import com.example.niteshverma.demoweather.interfaces.RecyclerItemTouchHelper;
import com.example.niteshverma.demoweather.model.Bookmark;
import com.example.niteshverma.demoweather.viewmodel.BookmarkListViewModel;

import java.util.List;

public class BookmarkListFragment extends BaseFragment implements BookmarkListAdapter.OnItemClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private Button btnRefresh;
    private CoordinatorLayout coordinatorLayout;
    protected RecyclerView recyclerView;
    private FloatingActionButton fabAdd;

    private BookmarkListAdapter mAdapter;
    private BookmarkListViewModel bookmarkListViewModel;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_bookmark_list;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_item_bookmark_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_help:
                showHelpDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((DashboardActivity) getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle(R.string.app_name);

        bookmarkListViewModel = ViewModelProviders.of(this).get(BookmarkListViewModel.class);

        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        fabAdd = view.findViewById(R.id.fab_add);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnRefresh = view.findViewById(R.id.btnRefresh);

        initViews();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkListViewModel.refresh();
            }
        });
        addObservers();

        if(PreferenceManager.getInstance().isHelpDialogFirstTime(DemoApplication.get())){
            showHelpDialog();
            PreferenceManager.getInstance().setIsFirstTImeHelp(DemoApplication.get(), false);
        }
    }

    private void addObservers(){
        bookmarkListViewModel.getAllBookmarkList().observe(this, new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(@Nullable List<Bookmark> bookmarks) {
                refreshWithLocation(bookmarks);
            }
        });
    }

    public void refreshWithLocation(Bookmark bookmark){
        if(mAdapter != null){
            mAdapter.addItems(bookmark);
        }
    }

    public void refreshWithLocation(List<Bookmark> bookmarkList){
        if(mAdapter != null){
            mAdapter.setItems(bookmarkList);
        }
    }

    private void initViews() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BookmarkListAdapter(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddLocationDialog();
            }
        });
    }

    private void showAddLocationDialog(){
        AddLocationDialogFragment addLocationDialogFragment = AddLocationDialogFragment.getInstance();
        addLocationDialogFragment.show(getChildFragmentManager(), AddLocationDialogFragment.class.getSimpleName());
    }

    private void showHelpDialog(){
        HelpDialogFragment helpDialogFragment = HelpDialogFragment.getInstance();
        helpDialogFragment.show(getChildFragmentManager(), HelpDialogFragment.class.getSimpleName());
    }

    @Override
    public void onItemClicked(View v, int position) {
        ((DashboardActivity)getActivity()).addFragment(BookmarkDetailFragment.getInstance(mAdapter.getItems().get(position)));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof BookmarkListAdapter.BookmarkViewHolder) {

            final int deletedIndex = viewHolder.getAdapterPosition();
            final Bookmark bookmarkToDelete = mAdapter.getItems().get(deletedIndex);
            bookmarkToDelete.setDeleteIndex(deletedIndex);
            bookmarkToDelete.setDeleted(false);
            refreshViewAndDeleteItem(bookmarkToDelete);
        }
    }

    private void refreshViewAndDeleteItem(final Bookmark bookmark){
        Snackbar snackbar;
        String message;
        mAdapter.removeItem(bookmark);
        message = bookmark.getLocationName() + " removed from bookmark.";

        snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.restoreItem(bookmark, bookmark.getDeleteIndex());
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if(event != Snackbar.Callback.DISMISS_EVENT_ACTION){
                    DBHelper.getInstance().deleteBookmark(bookmark, DemoApplication.get());
                }
            }

            @Override
            public void onShown(Snackbar snackbar) {
            }
        });
        snackbar.show();
    }
}
