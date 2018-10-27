package com.example.niteshverma.demoweather.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.niteshverma.demoweather.R;
import com.example.niteshverma.demoweather.Utility.Utilities;
import com.example.niteshverma.demoweather.activity.DashboardActivity;
import com.example.niteshverma.demoweather.adapter.Forecast5DayAdapter;
import com.example.niteshverma.demoweather.model.Bookmark;
import com.example.niteshverma.demoweather.model.Forecast;
import com.example.niteshverma.demoweather.model.Forecast5Day;
import com.example.niteshverma.demoweather.viewmodel.BookmarkDetailViewModel;

public class BookmarkDetailFragment extends BaseFragment{

    private BookmarkDetailViewModel bookmarkDetailViewModel;

    private TextView tvTemperature;
    private TextView tvHumidity;
    private TextView tvWeather;
    private TextView tvWind;
    private TextView tvSunrise;
    private TextView tvSunset;
    private TextView tvPressure;

    private TextView temp;
    private TextView weather_main;
    private ImageView weather_icon;

    private ProgressBar progressBar;
    private CardView cardView5DF;

    private RecyclerView recyclerViewForcast5Day;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_bookmark_detail;
    }

    public static BookmarkDetailFragment getInstance(Bookmark bookmark){
        BookmarkDetailFragment bookmarkDetailFragment = new BookmarkDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bookmark", bookmark);
        bookmarkDetailFragment.setArguments(bundle);
        return bookmarkDetailFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookmarkDetailViewModel = ViewModelProviders.of(this).get(BookmarkDetailViewModel.class);

        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((DashboardActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


        tvTemperature = view.findViewById(R.id.tvTemperature);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvWeather = view.findViewById(R.id.tvWeather);
        tvWind = view.findViewById(R.id.tvWind);
        tvSunrise = view.findViewById(R.id.tvSunrise);
        tvSunset = view.findViewById(R.id.tvSunset);
        tvPressure = view.findViewById(R.id.tvPressure);

        temp = view.findViewById(R.id.temp);
        weather_main = view.findViewById(R.id.weather_main);
        weather_icon = view.findViewById(R.id.weather_icon);

        progressBar = view.findViewById(R.id.progressBar);
        cardView5DF = view.findViewById(R.id.cardView5DF);

        recyclerViewForcast5Day = view.findViewById(R.id.recyclerViewForcast5Day);
        recyclerViewForcast5Day.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewForcast5Day.setLayoutManager(mLayoutManager);
        recyclerViewForcast5Day.setNestedScrollingEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
        cardView5DF.setVisibility(View.GONE);

        final Bookmark bookmark = (Bookmark) getArguments().getSerializable("bookmark");
        getActivity().setTitle(bookmark.getLocationName());

        getTodayForecast(bookmark);
        get5DayForecast(bookmark);
    }

    private void getTodayForecast(Bookmark bookmark){
        bookmarkDetailViewModel.getTodaysForecast(bookmark).observe(this, new Observer<Forecast>() {
            @Override
            public void onChanged(@Nullable Forecast todayForecast) {
                tvTemperature.setText(String.format(getString(R.string.msg_temperature_c), todayForecast.getMain().getTemp()));
                tvHumidity.setText(String.format(getString(R.string.msg_humidity), todayForecast.getMain().getHumidity()));
                tvWeather.setText(todayForecast.getWeather().get(0).getDescription());
                tvWind.setText( String.format(getString(R.string.msg_wind), todayForecast.getWind().getSpeed(), todayForecast.getWind().getDeg()));
                tvSunrise.setText(Utilities.getFormatedTime(todayForecast.getSys().getSunrise()));
                tvSunset.setText(Utilities.getFormatedTime(todayForecast.getSys().getSunset()));
                tvPressure.setText(String.format(getString(R.string.msg_pressure), todayForecast.getMain().getPressure()));


                temp.setText(String.valueOf(todayForecast.getMain().getTemp()));
                weather_main.setText(String.valueOf(todayForecast.getWeather().get(0).getMain()));
                String iconStr = "w_" + todayForecast.getWeather().get(0).getIcon();
                int id = getContext().getResources().getIdentifier(iconStr, "drawable", getContext().getPackageName());
                if(id > 0){
                    weather_icon.setImageResource(id);
                }
            }
        });
    }

    private void get5DayForecast(Bookmark bookmark){
        bookmarkDetailViewModel.getFiveDayForecast(bookmark).observe(this, new Observer<Forecast5Day>() {
            @Override
            public void onChanged(@Nullable Forecast5Day forecast5Day) {
                add5DayForeCast(forecast5Day);
            }
        });
    }

    private void add5DayForeCast(Forecast5Day forecast5Day){

        if(forecast5Day != null && forecast5Day.getList() != null && forecast5Day.getList().size() > 0){

            Forecast5DayAdapter adapter = new Forecast5DayAdapter();
            recyclerViewForcast5Day.setAdapter(adapter);
            adapter.setItems(forecast5Day.getList());
            adapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);
            cardView5DF.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            cardView5DF.setVisibility(View.GONE);
        }
    }
}
