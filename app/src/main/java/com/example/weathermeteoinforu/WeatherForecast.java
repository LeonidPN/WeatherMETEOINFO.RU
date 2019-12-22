package com.example.weathermeteoinforu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weathermeteoinforu.Database.DBHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class WeatherForecast extends AppCompatActivity {
    private ImageButton buttonBack;

    private DBHelper dbHelper;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        dbHelper = new DBHelper(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", 0);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBHelper.Tables.WeatherForecast.TABLE +
                " where " + DBHelper.Tables.WeatherForecast.Columnes.COLUMN_ID + " =?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
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

            ((TextView) findViewById(R.id.textViewDateWeatherForecast)).setText(date);
            ((TextView) findViewById(R.id.textViewWeatherDescription)).setText(weatherDescription);
            ((TextView) findViewById(R.id.textViewDayTemperature)).setText(temperatureDay + "\u00B0");
            ((TextView) findViewById(R.id.textViewNightTemperature)).setText(temperatureNight + "\u00B0");
            ((TextView) findViewById(R.id.textViewDayPressure)).setText(pressureDay + " мм. рт. ст.");
            ((TextView) findViewById(R.id.textViewNightPressure)).setText(pressureNight + " мм. рт. ст.");
            ((TextView) findViewById(R.id.textViewWetWeatherForecast)).setText(wet + " мм");
            ((TextView) findViewById(R.id.textViewWindDirectionWeatherForecast)).setText(windDirection);
            ((TextView) findViewById(R.id.textViewWindSpeedWeatherForecast)).setText(windSpeed + " м/с");
            ((TextView) findViewById(R.id.textViewRainChanceWeatherForecast)).setText(rainChance + " %");
            Bitmap bitmap = BitmapFactory.decodeFile(getFileStreamPath(imgSrc).getAbsolutePath());
            bitmap = Bitmap.createScaledBitmap(bitmap, 182, 182, false);
            ((ImageView) findViewById(R.id.imageViewWeather)).setImageBitmap(bitmap);
        }

        buttonBack = findViewById(R.id.imageButtonBackWeatherForecast);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
