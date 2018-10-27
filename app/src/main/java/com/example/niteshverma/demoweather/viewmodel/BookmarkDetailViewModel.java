package com.example.niteshverma.demoweather.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.niteshverma.demoweather.model.Bookmark;
import com.example.niteshverma.demoweather.model.Forecast;
import com.example.niteshverma.demoweather.model.Forecast5Day;
import com.example.niteshverma.demoweather.network.NetworkHelper;

public class BookmarkDetailViewModel extends ViewModel {

    private MutableLiveData<Forecast> todayForecastMutableLiveData;
    private MutableLiveData<Forecast5Day> fiveDayForecastMutableLiveData;

    public MutableLiveData<Forecast> getTodaysForecast(Bookmark bookmark){
        if(todayForecastMutableLiveData == null){
            todayForecastMutableLiveData = new MutableLiveData<>();
            loadTodayForecast(bookmark);
        }
        return todayForecastMutableLiveData;
    }

    public MutableLiveData<Forecast5Day> getFiveDayForecast(Bookmark bookmark){
        if(fiveDayForecastMutableLiveData == null){
            fiveDayForecastMutableLiveData = new MutableLiveData<>();
            load5DayForcast(bookmark);
        }
        return fiveDayForecastMutableLiveData;
    }


    private void loadTodayForecast(final Bookmark bookmark){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkHelper networkHelper = new NetworkHelper();
                Forecast todayForecast = networkHelper.loadTodayForcast(bookmark);
                if(todayForecastMutableLiveData != null){
                    todayForecastMutableLiveData.postValue(todayForecast);
                }
            }
        });
        t.start();
    }

    private void load5DayForcast(final Bookmark bookmark){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkHelper networkHelper = new NetworkHelper();
                Forecast5Day forecast = networkHelper.load5DayForcast(bookmark);
                if(fiveDayForecastMutableLiveData != null){
                    fiveDayForecastMutableLiveData.postValue(forecast);
                }
            }
        });
        t.start();
    }

}
