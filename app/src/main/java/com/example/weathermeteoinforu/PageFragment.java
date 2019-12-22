package com.example.weathermeteoinforu;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weathermeteoinforu.Database.DBHelper;
import com.example.weathermeteoinforu.Database.WeatherForecastModel;
import com.example.weathermeteoinforu.Database.WeatherModel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class PageFragment extends Fragment {
    private int resource;
    private SwipeRefreshLayout swipeRefreshLayout;

    private WeatherModel weather;
    private ArrayList<WeatherForecastModel> weatherForecast;

    private DBHelper dbHelper;

    private SharedPreferences settings;

    private Context context;

    private View view;

    public PageFragment(int resource) {
        this.resource = resource;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(resource, container, false);
        this.view = view;
        dbHelper = new DBHelper(view.getContext());

        context = view.getContext();

        settings = view.getContext().getSharedPreferences("PreferencesWeatherForecast",
                Context.MODE_PRIVATE);

        //updateData();
        setData(container.getRootView());

        if (resource == R.layout.weather_in_week_page) {
            final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(new WeatherInWeekListAdapter(weatherForecast));

            swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateData();
                    setData(view);
                    recyclerView.setAdapter(new WeatherInWeekListAdapter(weatherForecast));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        if (resource == R.layout.current_weather_page) {
            final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            ArrayList<WeatherModel> list = new ArrayList<>();
            list.add(weather);
            recyclerView.setAdapter(new WeatherCurrentAdapter(list));

            swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateData();
                    setData(view);
                    ArrayList<WeatherModel> list = new ArrayList<>();
                    list.add(weather);
                    recyclerView.setAdapter(new WeatherCurrentAdapter(list));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        return view;
    }

    private void updateData() {
        String message = null;
        try {
            message = new UpdateData(context).execute((Void) null).get();
            if (message != null) {
                Toast.makeText(context, message, Toast.LENGTH_LONG);
            }

            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd   HH:mm");

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Date_Update", formatForDateNow.format(new Date()));
            editor.commit();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setData(View view) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.Tables.Weather.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(DBHelper.Tables.Weather.Columnes.COLUMN_ID);
            int weatherDescriptionColumnIndex = cursor.getColumnIndex(DBHelper.Tables.Weather.Columnes.COLUMN_WEATHER_DESCRIPTION);
            int temperatureColumnIndex = cursor.getColumnIndex(DBHelper.Tables.Weather.Columnes.COLUMN_TEMPERATURE);
            int pressureColumnIndex = cursor.getColumnIndex(DBHelper.Tables.Weather.Columnes.COLUMN_PRESSURE);
            int wetColumnIndex = cursor.getColumnIndex(DBHelper.Tables.Weather.Columnes.COLUMN_WET);
            int windDirectionColumnIndex = cursor.getColumnIndex(DBHelper.Tables.Weather.Columnes.COLUMN_WIND_DIRECTION);
            int windSpeedColumnIndex = cursor.getColumnIndex(DBHelper.Tables.Weather.Columnes.COLUMN_WIND_SPEED);
            int rainChanceColumnIndex = cursor.getColumnIndex(DBHelper.Tables.Weather.Columnes.COLUMN_RAIN_CHANCE);
            int imgSrcColumnIndex = cursor.getColumnIndex(DBHelper.Tables.Weather.Columnes.COLUMN_IMG_SRC);

            int id = cursor.getInt(idColumnIndex);
            String weatherDescription = cursor.getString(weatherDescriptionColumnIndex);
            String temperature = cursor.getString(temperatureColumnIndex);
            String pressure = cursor.getString(pressureColumnIndex);
            String wet = cursor.getString(wetColumnIndex);
            String windDirection = cursor.getString(windDirectionColumnIndex);
            String windSpeed = cursor.getString(windSpeedColumnIndex);
            String rainChance = cursor.getString(rainChanceColumnIndex);
            String imgSrc = cursor.getString(imgSrcColumnIndex);
            weather = new WeatherModel(id, weatherDescription, temperature, pressure, wet, windDirection,
                    windSpeed, rainChance, imgSrc);
            cursor.close();
        } else {
            cursor.close();
        }

        cursor = db.query(DBHelper.Tables.WeatherForecast.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_ID);
            int dateColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_DATE);
            int weatherDescriptionColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WEATHER_DESCRIPTION);
            int temperatureDayColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_TEMPERATURE_DAY);
            int temperatureNightColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_TEMPERATURE_NIGHT);
            int pressureDayColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_PRESSURE_DAY);
            int pressureNightColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_PRESSURE_NIGHT);
            int wetColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WET);
            int windDirectionColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WIND_DIRECTION);
            int windSpeedColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WIND_SPEED);
            int rainChanceColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_RAIN_CHANCE);
            int imgSrcColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_IMG_SRC);
            weatherForecast = new ArrayList<>();
            do {
                int id = cursor.getInt(idColumnIndex);
                String date = cursor.getString(dateColumnIndex);
                String weatherDescription = cursor.getString(weatherDescriptionColumnIndex);
                String temperatureDay = cursor.getString(temperatureDayColumnIndex);
                String temperatureNight = cursor.getString(temperatureNightColumnIndex);
                String pressureDay = cursor.getString(pressureDayColumnIndex);
                String pressureNight = cursor.getString(pressureNightColumnIndex);
                String wet = cursor.getString(wetColumnIndex);
                String windDirection = cursor.getString(windDirectionColumnIndex);
                String windSpeed = cursor.getString(windSpeedColumnIndex);
                String rainChance = cursor.getString(rainChanceColumnIndex);
                String imgSrc = cursor.getString(imgSrcColumnIndex);
                weatherForecast.add(new WeatherForecastModel(id, date, weatherDescription, temperatureDay,
                        temperatureNight, pressureDay, pressureNight, wet, windDirection, windSpeed, rainChance, imgSrc));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            cursor.close();
        }

        String dateUpdate = settings.getString("Date_Update", "");
        ((TextView) view.getRootView().findViewById(R.id.textViewDateUpdate)).setText(dateUpdate);
    }

}
