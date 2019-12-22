package com.example.weathermeteoinforu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weathermeteoinforu.Database.WeatherModel;

import java.util.ArrayList;

public class WeatherCurrentAdapter extends RecyclerView.Adapter<WeatherCurrentAdapter.WeatherCurrentViewHolder>  {

    private ArrayList<WeatherModel> weather;

    public static class WeatherCurrentViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public WeatherCurrentViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public WeatherCurrentAdapter(ArrayList<WeatherModel> weather) {
        this.weather = weather;
    }

    @Override
    public WeatherCurrentViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.current_weather, parent, false);
        WeatherCurrentViewHolder holder = new WeatherCurrentViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(WeatherCurrentViewHolder holder, final int position) {
        View view = holder.view;

        TextView weatherDescription = view.findViewById(R.id.textViewWeatherDescription);
        TextView temperature = view.findViewById(R.id.textViewTemperature);
        TextView pressure = view.findViewById(R.id.textViewPressure);
        TextView wet = view.findViewById(R.id.textViewWet);
        TextView windDirection = view.findViewById(R.id.textViewWindDirection);
        TextView windSpeed = view.findViewById(R.id.textViewWindSpeed);
        TextView rainChance = view.findViewById(R.id.textViewRainChance);

        WeatherModel weatherModel=weather.get(position);

        weatherDescription.setText(weatherModel.getWeatherDescription());
        temperature.setText(weatherModel.getTemperature() + "\u00B0");
        pressure.setText(weatherModel.getPressure() + " мм. рт. ст.");
        wet.setText(weatherModel.getWet() + " %");
        windDirection.setText(weatherModel.getWindDirection());
        windSpeed.setText(weatherModel.getWindSpeed() + " м/с");
        rainChance.setText(weatherModel.getRainChance() + " мм.");

        Bitmap bitmap = BitmapFactory.decodeFile(holder.view.getContext()
                .getFileStreamPath(weather.get(position).getImgSrc()).getAbsolutePath());
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        ((ImageView) holder.view.findViewById(R.id.imageViewWeather)).setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return weather.size();
    }
}
