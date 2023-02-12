package com.project.agroworld.weather.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ForecastItemLayoutBinding;
import com.project.agroworld.weather.listener.WeatherForecastListener;
import com.project.agroworld.weather.model.weatherlist.ListItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherForecastViewHolder extends RecyclerView.ViewHolder {
    private final ForecastItemLayoutBinding binding;

    public WeatherForecastViewHolder(@NonNull ForecastItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindForecastData(ListItem item, WeatherForecastListener listener) {
        String iconUrl = "http://openweathermap.org/img/wn/" + item.getWeather().get(0).getIcon() + "@4x.png";
        String mainTemp = String.format("%.0f", (item.getMain().getTemp() + 0.01) - 273.15);
        String [] dates = item.getDtTxt().split(" ");
        binding.tvForecastDate.setText(dates[0] + "\n" + dates[1]);
        binding.tvForecastTemp.setText(mainTemp + "°C");
        binding.tvForecastStatus.setText(item.getWeather().get(0).getDescription());
        Glide.with(binding.ivForecastIcon).load(iconUrl).placeholder(R.drawable.weather).dontAnimate().into(binding.ivForecastIcon);

        binding.crdForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onForecastWeatherCardClick(item, getAbsoluteAdapterPosition());
            }
        });

    }
}
