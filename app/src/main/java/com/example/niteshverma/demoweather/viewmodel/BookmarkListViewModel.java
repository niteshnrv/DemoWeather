package com.example.niteshverma.demoweather.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.niteshverma.demoweather.DemoApplication;
import com.example.niteshverma.demoweather.database.DBHelper;
import com.example.niteshverma.demoweather.model.Bookmark;

import java.util.ArrayList;
import java.util.List;

public class BookmarkListViewModel extends ViewModel {

    private MutableLiveData<List<Bookmark>> bookmarkListMutableLiveData;

    public LiveData<List<Bookmark>> getAllBookmarkList(){
        if(bookmarkListMutableLiveData == null){
            bookmarkListMutableLiveData = new MutableLiveData<>();
            loadBookmarkFromDB();
        }
        return bookmarkListMutableLiveData;
    }

    public void refresh(){
        loadBookmarkFromDB();
    }

    private void loadBookmarkFromDB(){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Bookmark> items  = DBHelper.getInstance().getAllBookmark(DemoApplication.get());
                bookmarkListMutableLiveData.postValue(items);
            }
        });
        t.start();
    }

}
