package com.example.niteshverma.demoweather.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niteshverma.demoweather.R;
import com.example.niteshverma.demoweather.model.Bookmark;
import com.example.niteshverma.demoweather.model.Forecast;

import java.util.ArrayList;
import java.util.List;

public class Forecast5DayAdapter extends RecyclerView.Adapter<Forecast5DayAdapter.ForecastViewHolder>{

    private List<Forecast> itemList = new ArrayList<>();

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_5_day_forcast_item,
                parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int i) {
        Forecast forecast = itemList.get(i);
        String iconStr = "w_" + forecast.getWeather().get(0).getIcon();
        int id = holder.icon.getContext().getResources().getIdentifier(iconStr, "drawable", holder.icon.getContext().getPackageName());
        if(id > 0){
            holder.icon.setImageResource(id);
        }
        String dateText = forecast.getDt_txt().replace(" ", "\n");
        holder.tvDate.setText(dateText);
        holder.tvTempMax.setText(String.format(holder.icon.getContext().getString(R.string.msg_temperature_c), forecast.getMain().getTemp_max()));
        holder.tvTempMin.setText(String.format(holder.icon.getContext().getString(R.string.msg_temperature_c), forecast.getMain().getTemp_min()));
        holder.tvWind.setText( String.format(holder.icon.getContext().getString(R.string.msg_wind), forecast.getWind().getSpeed(), forecast.getWind().getDeg()));
        holder.tvCloudPressure.setText(String.format(holder.icon.getContext().getString(R.string.msg_cloud_pressure), forecast.getClouds().getAll(), forecast.getMain().getPressure()));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<Forecast> results) {
        itemList = results;
        notifyDataSetChanged();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView tvDate;
        TextView tvTempMax;
        TextView tvTempMin;
        TextView tvWind;
        TextView tvCloudPressure;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTempMax = itemView.findViewById(R.id.tvTempMax);
            tvTempMin = itemView.findViewById(R.id.tvTempMin);
            tvWind = itemView.findViewById(R.id.tvWind);
            tvCloudPressure = itemView.findViewById(R.id.tvCloudPressure);
        }
    }
}
