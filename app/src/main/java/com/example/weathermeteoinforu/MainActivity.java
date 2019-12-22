package com.example.weathermeteoinforu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private ImageButton buttonPreferences;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("PreferencesWeatherForecast",
                Context.MODE_PRIVATE);

        buttonPreferences = findViewById(R.id.imageButtonPreferences);
        buttonPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PreferencesActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        ViewPager viewPager = findViewById(R.id.viewPager);
        WeatherFragmentPagerAdapter adapter = new WeatherFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PageFragment(R.layout.current_weather_page), "Текущая погода");
        adapter.addFragment(new PageFragment(R.layout.weather_in_week_page), "Прогноз на неделю");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (settings.getBoolean("Data_changed", true)) {
            ViewPager viewPager = findViewById(R.id.viewPager);
            WeatherFragmentPagerAdapter adapter = new WeatherFragmentPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new PageFragment(R.layout.current_weather_page), "Текущая погода");
            adapter.addFragment(new PageFragment(R.layout.weather_in_week_page), "Прогноз на неделю");
            viewPager.setAdapter(adapter);

            TabLayout tabLayout = findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("Data_changed", false);
            editor.commit();
        }
    }
}
