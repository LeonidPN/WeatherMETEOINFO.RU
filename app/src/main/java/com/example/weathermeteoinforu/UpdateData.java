package com.example.weathermeteoinforu;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.weathermeteoinforu.Database.DBHelper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateData extends AsyncTask<Void, Void, String> {

    private String urlLinkForecast;
    private String urlLinkWeather;

    private Context context;

    private SharedPreferences settings;

    private DBHelper dbHelper;

    public UpdateData(Context context) {
        this.context = context;
    }

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
        dbHelper = new DBHelper(context);
        settings = context.getSharedPreferences("PreferencesWeatherForecast",
                Context.MODE_PRIVATE);
        String country = settings.getString("Country_code", "");
        String state = settings.getString("State_code", "");
        String city = settings.getString("City_code", "");
        urlLinkForecast = "http://old.meteoinfo.ru/forecasts5000" + country + state + city;
        urlLinkWeather = "http://old.meteoinfo.ru/pogoda" + country + state + city;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Connection.Response execute = Jsoup.connect(urlLinkForecast).execute();
            Document forecast = Jsoup.parse(execute.body());

            String[] dateList = new String[7];
            String[] weatherDescriptionList = new String[7];
            String[] temperatureDayList = new String[7];
            String[] temperatureNightList = new String[7];
            String[] pressureDayList = new String[7];
            String[] pressureNightList = new String[7];
            String[] wetList = new String[7];
            String[] windDirectionList = new String[7];
            String[] windSpeedList = new String[7];
            String[] rainChanceList = new String[7];
            String[] imgSrcList = new String[7];

            int i;
            Elements rows = forecast.select("tr");

            Element row = rows.get(22);
            Element elem = row.select("td").get(2);
            dateList[0] = elem.text().split(" ")[0] + "\n" + elem.selectFirst("nobr").text();
            Elements elements = row.select("td.pogodadate");
            i = 1;
            for (Element element : elements) {
                dateList[i] = element.text().split(" ")[0] + "\n" + element.selectFirst("nobr").text();
                i++;
            }

            row = rows.get(24);
            elements = row.select("td.pogodacell");
            i = 0;
            for (Element element : elements) {
                String[] e = element.text().split(" &nbsp;/&nbsp; ")[0].split(" ⁄ ");
                pressureNightList[i] = e[0];
                pressureDayList[i] = e[1];
                i++;
            }

            row = rows.get(25);
            elements = row.select("td.pogodacell");
            i = 0;
            for (Element element : elements) {
                String[] e = element.text().split(" &nbsp;/&nbsp; ")[0].split(" ⁄ ");
                temperatureNightList[i] = e[0];
                temperatureDayList[i] = e[1];
                i++;
            }

            row = rows.get(26);
            elements = row.select("td.ww");
            i = 0;
            for (Element element : elements) {
                String src = element.selectFirst("img").attr("src").split("/")[3];
                imgSrcList[i] = src;
                i++;
            }

            row = rows.get(27);
            elements = row.select("td.pogodacell");
            i = 0;
            for (Element element : elements) {
                weatherDescriptionList[i] = element.text();
                i++;
            }

            row = rows.get(27);
            elements = row.select("td.pogodacell");
            i = 0;
            for (Element element : elements) {
                weatherDescriptionList[i] = element.text();
                i++;
            }

            row = rows.get(28);
            elements = row.select("td.pogodacell");
            i = 0;
            for (Element element : elements) {
                wetList[i] = element.text();
                i++;
            }

            row = rows.get(29);
            elements = row.select("td.pogodacell");
            i = 0;
            for (Element element : elements) {
                rainChanceList[i] = element.text();
                i++;
            }

            row = rows.get(30);
            elements = row.select("td.pogodacell");
            i = 0;
            for (Element element : elements) {
                windDirectionList[i] = element.text();
                i++;
            }

            row = rows.get(31);
            elements = row.select("td.pogodacell");
            i = 0;
            for (Element element : elements) {
                windSpeedList[i] = element.text();
                i++;
            }

            for (int j = 0; j < imgSrcList.length; j++) {
                boolean flag = true;
                for (String fileName : context.fileList()) {
                    if (fileName.equals(imgSrcList[j])) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    saveFile(imgSrcList[j]);
                }
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(DBHelper.Tables.WeatherForecast.TABLE, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                i = 0;
                int idColumnIndex = cursor.getColumnIndex(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_ID);
                do {
                    ContentValues cv = new ContentValues();
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_DATE, dateList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WEATHER_DESCRIPTION, weatherDescriptionList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_TEMPERATURE_DAY, temperatureDayList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_TEMPERATURE_NIGHT, temperatureNightList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_PRESSURE_DAY, pressureDayList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_PRESSURE_NIGHT, pressureNightList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WET, wetList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WIND_DIRECTION, windDirectionList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WIND_SPEED, windSpeedList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_RAIN_CHANCE, rainChanceList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_IMG_SRC, imgSrcList[i]);
                    int id = cursor.getInt(idColumnIndex);
                    db.update(DBHelper.Tables.WeatherForecast.TABLE, cv,
                            DBHelper.Tables.WeatherForecast.Columnes.COLUMN_ID + "=" + id, null);
                    i++;
                } while (cursor.moveToNext());
            } else {
                for (i = 0; i < 7; i++) {
                    ContentValues cv = new ContentValues();
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_DATE, dateList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WEATHER_DESCRIPTION, weatherDescriptionList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_TEMPERATURE_DAY, temperatureDayList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_TEMPERATURE_NIGHT, temperatureNightList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_PRESSURE_DAY, pressureDayList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_PRESSURE_NIGHT, pressureNightList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WET, wetList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WIND_DIRECTION, windDirectionList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_WIND_SPEED, windSpeedList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_RAIN_CHANCE, rainChanceList[i]);
                    cv.put(DBHelper.Tables.WeatherForecast.Columnes.COLUMN_IMG_SRC, imgSrcList[i]);
                    db.insert(DBHelper.Tables.WeatherForecast.TABLE, null, cv);
                }
            }
            cursor.close();

            String weatherDescription = weatherDescriptionList[0];
            String temperature = "";
            String pressure = "";
            String wet = "";
            String windDirection = "";
            String windSpeed = "";
            String rainChance = rainChanceList[0];
            String imgSrc = imgSrcList[0];

            execute = Jsoup.connect(urlLinkWeather).execute();
            Document weather = Jsoup.parse(execute.body());

            rows = weather.select("tr");

            for (Element r : rows) {
                Element element = r.selectFirst("th.pogodacell2");
                if (element != null) {
                    if (element.text().equals("Атмосферное давление на уровне станции, мм рт.ст.")) {
                        element = r.selectFirst("td.pogodacell");
                        pressure = element.text();
                    }
                    if (element.text().equals("Температура воздуха, °C")) {
                        element = r.selectFirst("td.pogodacell");
                        temperature = element.text();
                    }
                    if (element.text().equals("Относительная влажность, %")) {
                        element = r.selectFirst("td.pogodacell");
                        wet = element.text();
                    }
                    if (element.text().equals("Направление ветра")) {
                        element = r.selectFirst("td.pogodacell");
                        windDirection = element.text();
                    }
                    if (element.text().equals("Скорость ветра, м/с")) {
                        element = r.selectFirst("td.pogodacell");
                        windSpeed = element.text();
                    }
                }
            }

            if (pressure.equals("")) {
                pressure = pressureDayList[0];
            }
            if (temperature.equals("")) {
                temperature = temperatureDayList[0];
            }
            if (wet.equals("")) {
                wet = rainChanceList[0];
            }
            if (windDirection.equals("")) {
                windDirection = windDirectionList[0];
            }
            if (windSpeed.equals("")) {
                windSpeed = windSpeedList[0];
            }

            cursor = db.query(DBHelper.Tables.Weather.TABLE, null, null, null, null, null, null);
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.Tables.Weather.Columnes.COLUMN_WEATHER_DESCRIPTION, weatherDescription);
            cv.put(DBHelper.Tables.Weather.Columnes.COLUMN_TEMPERATURE, temperature);
            cv.put(DBHelper.Tables.Weather.Columnes.COLUMN_PRESSURE, pressure);
            cv.put(DBHelper.Tables.Weather.Columnes.COLUMN_WET, wet);
            cv.put(DBHelper.Tables.Weather.Columnes.COLUMN_WIND_DIRECTION, windDirection);
            cv.put(DBHelper.Tables.Weather.Columnes.COLUMN_WIND_SPEED, windSpeed);
            cv.put(DBHelper.Tables.Weather.Columnes.COLUMN_RAIN_CHANCE, rainChance);
            cv.put(DBHelper.Tables.Weather.Columnes.COLUMN_IMG_SRC, imgSrc);
            if (cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex(DBHelper.Tables.Weather.Columnes.COLUMN_ID);
                int id = cursor.getInt(idColumnIndex);
                db.update(DBHelper.Tables.Weather.TABLE, cv,
                        DBHelper.Tables.Weather.Columnes.COLUMN_ID + "=" + id, null);
            } else {
                db.insert(DBHelper.Tables.Weather.TABLE, null, cv);
            }
            cursor.close();

            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd   HH:mm");

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Date_Update", formatForDateNow.format(new Date()));
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }

    private void saveFile(String fileName) {
        InputStream input = null;
        FileOutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://old.meteoinfo.ru/images/pr5000_images/" + fileName);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                input = connection.getInputStream();
                output = context.openFileOutput(fileName, Context.MODE_PRIVATE);

                byte data[] = new byte[4096];
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                    }
                    output.write(data, 0, count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }
            if (connection != null)
                connection.disconnect();
        }
    }
}
