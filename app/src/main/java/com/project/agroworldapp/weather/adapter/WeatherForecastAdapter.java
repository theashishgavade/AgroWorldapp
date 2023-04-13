package com.project.agroworldapp.weather.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.databinding.ForecastItemLayoutBinding;
import com.project.agroworldapp.weather.listener.WeatherForecastListener;
import com.project.agroworldapp.weather.model.weatherlist.ListItem;
import com.project.agroworldapp.weather.viewholder.WeatherForecastViewHolder;

import java.util.List;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastViewHolder> {

    private final List<ListItem> weatherForecastItemList;
    private final WeatherForecastListener listener;

    public WeatherForecastAdapter(List<ListItem> weatherForecastItemList, WeatherForecastListener listener) {
        this.weatherForecastItemList = weatherForecastItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeatherForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherForecastViewHolder(ForecastItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherForecastViewHolder holder, int position) {
        ListItem listItemModel = weatherForecastItemList.get(position);
        holder.bindForecastData(listItemModel, listener);
    }

    @Override
    public int getItemCount() {
        return weatherForecastItemList.size();
    }
}
