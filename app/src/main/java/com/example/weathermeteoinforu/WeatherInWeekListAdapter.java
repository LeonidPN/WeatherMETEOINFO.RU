package com.example.weathermeteoinforu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weathermeteoinforu.Database.WeatherForecastModel;

import java.util.ArrayList;

public class WeatherInWeekListAdapter extends RecyclerView.Adapter<WeatherInWeekListAdapter.WeatherInWeekListViewHolder> {

    private ArrayList<WeatherForecastModel> list;

    public static class WeatherInWeekListViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public WeatherInWeekListViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public WeatherInWeekListAdapter(ArrayList<WeatherForecastModel> list) {
        this.list = list;
    }

    @Override
    public WeatherInWeekListViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_in_week_list_item, parent, false);
        WeatherInWeekListViewHolder holder = new WeatherInWeekListViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(WeatherInWeekListViewHolder holder, final int position) {
        ((TextView) holder.view.findViewById(R.id.textViewDate)).setText(list.get(position).getDate());
        ((TextView) holder.view.findViewById(R.id.textViewDayTemperature))
                .setText(list.get(position).getTemperatureDay() + "\u00B0");
        ((TextView) holder.view.findViewById(R.id.textViewNightTemperature))
                .setText(list.get(position).getTemperatureNight() + "\u00B0");
        Bitmap bitmap = BitmapFactory.decodeFile(holder.view.getContext()
                .getFileStreamPath(list.get(position).getImgSrc()).getAbsolutePath());
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        ((ImageView) holder.view.findViewById(R.id.imageViewWeather)).setImageBitmap(bitmap);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WeatherForecast.class);
                intent.putExtra("ID", list.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
