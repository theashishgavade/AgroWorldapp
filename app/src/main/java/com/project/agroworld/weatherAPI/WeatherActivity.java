package com.project.agroworld.weatherAPI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.project.agroworld.R;
import com.project.agroworld.weatherAPI.HttpRequest.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    // initialize all buttons, textView, etc:

    ImageView weatherPNG;
    String adminArea;
    TextView addressT, updated_atT, statusT, tempT, temp_minTxt, temp_maxT, sunriseT, sunsetT, windT, latitudeT, longitudeT, pressureT;
    EditText CITY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Intent intent = getIntent();
        adminArea = intent.getStringExtra("adminArea");
        if (adminArea != null){
            new WeatherTask().execute(adminArea);
        }else {
            new WeatherTask().execute("Raigad");
        }
        
        //bellow all info view hide here
        findViewById(R.id.wdLatitude).setVisibility(View.GONE);
        findViewById(R.id.wdSunrise).setVisibility(View.GONE);
        findViewById(R.id.wdSunset).setVisibility(View.GONE);
        findViewById(R.id.wdWind).setVisibility(View.GONE);
        findViewById(R.id.wdLongitude).setVisibility(View.GONE);
        findViewById(R.id.weatherPNG).setVisibility(View.GONE);
        findViewById(R.id.wdPressure).setVisibility(View.GONE);

    }

    class WeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String[] args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + adminArea + "&units=metric&appid=cff3614749a8d630f28ea5c7f079d389");
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            addressT = findViewById(R.id.address);
            updated_atT = findViewById(R.id.updated_at);
            statusT = findViewById(R.id.status);
            tempT = findViewById(R.id.temp);
            temp_minTxt = findViewById(R.id.temp_min);
            temp_maxT = findViewById(R.id.temp_max);
            sunriseT = findViewById(R.id.sunrise);
            sunsetT = findViewById(R.id.sunset);
            windT = findViewById(R.id.wind);
            latitudeT = findViewById(R.id.latitude);
            longitudeT = findViewById(R.id.longitude);
            weatherPNG = findViewById(R.id.weatherPNG);
            pressureT = findViewById(R.id.pressure);

            try {

                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject coord = jsonObj.getJSONObject("coord");

                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                Glide.with(WeatherActivity.this).load("http://openweathermap.org/img/wn/" + weather.getString("icon") + "@4x.png").into(weatherPNG);
                long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                double latitude = coord.getDouble("lat");
                double longitude = coord.getDouble("lon");
                String pressure = main.getString("pressure");
                long sunrise = sys.getLong("sunrise");
                long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");
                String address = sys.getString("country") + ", " + jsonObj.getString("name");

                addressT.setText(address);
                updated_atT.setText(updatedAtText);
                statusT.setText(weatherDescription.toUpperCase());
                tempT.setText(temp);
                temp_minTxt.setText(tempMin);
                temp_maxT.setText(tempMax);
                sunriseT.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                sunsetT.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                windT.setText(windSpeed);
                pressureT.setText(pressure);
                latitudeT.setText(latitude + "E");
                longitudeT.setText(longitude + "N");

                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
                findViewById(R.id.wdLatitude).setVisibility(View.VISIBLE);
                findViewById(R.id.wdSunrise).setVisibility(View.VISIBLE);
                findViewById(R.id.wdSunset).setVisibility(View.VISIBLE);
                findViewById(R.id.wdWind).setVisibility(View.VISIBLE);
                findViewById(R.id.wdLongitude).setVisibility(View.VISIBLE);
                findViewById(R.id.weatherPNG).setVisibility(View.VISIBLE);
                findViewById(R.id.wdPressure).setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:

                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}