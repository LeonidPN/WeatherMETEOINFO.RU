package com.example.weathermeteoinforu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class PreferencesActivity extends AppCompatActivity {
    private ImageButton buttonBack;

    private Button buttonCountry;
    private Button buttonState;
    private Button buttonCity;

    private SharedPreferences settings;

    private HashMap<String, String> countries;
    private HashMap<String, String> states;
    private HashMap<String, String> cities;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        context = this;

        settings = getSharedPreferences("PreferencesWeatherForecast",
                Context.MODE_PRIVATE);

        countries = new HashMap<>();
        states = new HashMap<>();
        cities = new HashMap<>();

        buttonBack = findViewById(R.id.imageButtonBackPreferences);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonCountry = findViewById(R.id.buttonCountry);
        buttonState = findViewById(R.id.buttonState);
        buttonCity = findViewById(R.id.buttonCity);

        final Context context = this;

        updateButtonsText();

        updateInfo();

        buttonCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setTitle("Страна");
                final String[] list = new String[countries.size()];
                int i = 0;
                for (String key : countries.keySet()) {
                    list[i] = key;
                    i++;
                }
                Arrays.sort(list);
                ad.setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("Country", list[which]);
                        editor.putString("Country_code", "/" + countries.get(list[which]));
                        editor.putString("State_code", "");
                        editor.putString("City_code", "");
                        editor.putBoolean("Data_changed", true);
                        editor.commit();
                        updateInfo();
                    }
                });
                ad.setCancelable(true);
                ad.show();
            }
        });

        buttonState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setTitle("Область");
                final String[] list = new String[states.size()];
                int i = 0;
                for (String key : states.keySet()) {
                    list[i] = key;
                    i++;
                }
                Arrays.sort(list);
                ad.setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("State", list[which]);
                        editor.putString("State_code", "/" + states.get(list[which]));
                        editor.putString("City_code", "");
                        editor.putBoolean("Data_changed", true);
                        editor.commit();
                        updateInfo();
                    }
                });
                ad.setCancelable(true);
                ad.show();
            }
        });

        buttonCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setTitle("Город");
                final String[] list = new String[cities.size()];
                int i = 0;
                for (String key : cities.keySet()) {
                    list[i] = key;
                    i++;
                }
                Arrays.sort(list);
                ad.setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("City", list[which]);
                        editor.putString("City_code", "/" + cities.get(list[which]));
                        editor.putBoolean("Data_changed", true);
                        editor.commit();
                        updateButtonsText();
                    }
                });
                ad.setCancelable(true);
                ad.show();
            }
        });
    }

    private void updateButtonsText() {
        String country = settings.getString("Country", "");
        String state = settings.getString("State", "");
        String city = settings.getString("City", "");
        buttonCountry.setText(country);
        buttonState.setText(state);
        buttonCity.setText(city);
    }

    private void updateInfo() {
        if (!hasConnection()) {
            buttonCountry.setEnabled(false);
            buttonState.setEnabled(false);
            buttonCity.setEnabled(false);
        } else {
            try {
                String message = new UpdateLists().execute((Void) null).get();
                if (message != null) {
                    Toast.makeText(this, message, Toast.LENGTH_LONG);
                }
                SharedPreferences.Editor editor = settings.edit();
                if (settings.getString("Country_code", "").equals("/russia")) {
                    buttonState.setEnabled(true);
                    if (settings.getString("State_code", "").equals("")) {
                        String[] list = new String[states.size()];
                        int i = 0;
                        for (String key : states.keySet()) {
                            list[i] = key;
                            i++;
                        }
                        Arrays.sort(list);
                        editor.putString("State", list[0]);
                        editor.putString("State_code", "/" + states.get(list[0]));
                        editor.commit();
                        message = new UpdateLists().execute((Void) null).get();
                        if (message != null) {
                            Toast.makeText(this, message, Toast.LENGTH_LONG);
                        }
                    }
                } else {
                    buttonState.setEnabled(false);
                    editor.putString("State", "-");
                    editor.putString("State_code", "");
                    editor.commit();
                }
                if (settings.getString("City_code", "").equals("")) {
                    String[] list = new String[cities.size()];
                    int i = 0;
                    for (String key : cities.keySet()) {
                        list[i] = key;
                        i++;
                    }
                    Arrays.sort(list);
                    editor.putString("City", list[0]);
                    editor.putString("City_code", "/" + cities.get(list[0]));
                    editor.commit();
                }
                new UpdateData(context).execute((Void) null);
                updateButtonsText();
            } catch (ExecutionException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            } catch (InterruptedException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            }
        }
    }

    private boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private class UpdateLists extends AsyncTask<Void, Void, String> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
            /*SharedPreferences.Editor editor = settings.edit();
            editor.putString("Country_code", "/russia");
            editor.putString("State_code", "/ulyanovsk-area");
            editor.putString("City_code", "/ulyanovsk");
            editor.putString("Country", "Россия");
            editor.putString("State", "Ульяновская область");
            editor.putString("City", "Ульяновск");
            editor.commit();*/
            String country = settings.getString("Country_code", "");
            String state = settings.getString("State_code", "");
            String city = settings.getString("City_code", "");
            urlLink = "http://old.meteoinfo.ru/forecasts5000" + country + state + city;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Connection.Response execute = Jsoup.connect(urlLink).execute();
                Document document = Jsoup.parse(execute.body());

                Elements elements = document.select("select[name=\"countryCode\"]");
                if (countries == null || countries.size() == 0) {
                    countries = new HashMap<>();
                    for (Element element : elements.select("option")) {
                        String key = element.text();
                        String value = element.attr("value");
                        countries.put(key, value);
                    }
                }

                if (states == null || states.size() == 0) {
                    states = new HashMap<>();
                    elements = document.select("select[name=\"regionCode\"]");
                    for (Element element : elements.select("option")) {
                        String key = element.text();
                        String value = element.attr("value");
                        states.put(key, value);
                    }
                }

                elements = document.select("select[name=\"stationCode\"]");
                cities = new HashMap<>();
                for (Element element : elements.select("option")) {
                    String key = element.text();
                    String value = element.attr("value");
                    cities.put(key, value);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return null;
        }
    }
}
