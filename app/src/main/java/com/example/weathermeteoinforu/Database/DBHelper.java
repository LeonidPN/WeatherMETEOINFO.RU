package com.example.weathermeteoinforu.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weather.db";
    private static final int SCHEMA = 5;

    public static class Tables {
        public static class Weather {
            public static final String TABLE = "weather";

            public static class Columnes {
                public static final String COLUMN_ID = "id";
                public static final String COLUMN_WEATHER_DESCRIPTION = "weatherdescription";
                public static final String COLUMN_TEMPERATURE = "temperature";
                public static final String COLUMN_PRESSURE = "pressure";
                public static final String COLUMN_WET = "wet";
                public static final String COLUMN_WIND_DIRECTION = "winddirection";
                public static final String COLUMN_WIND_SPEED = "windspeed";
                public static final String COLUMN_RAIN_CHANCE = "rainchance";
                public static final String COLUMN_IMG_SRC = "imgsrc";
            }
        }

        public static class WeatherForecast {
            public static final String TABLE = "weatherforecast";

            public static class Columnes {
                public static final String COLUMN_ID = "id";
                public static final String COLUMN_DATE = "date";
                public static final String COLUMN_WEATHER_DESCRIPTION = "weatherdescription";
                public static final String COLUMN_TEMPERATURE_DAY = "temperatureday";
                public static final String COLUMN_TEMPERATURE_NIGHT = "temperaturenight";
                public static final String COLUMN_PRESSURE_DAY = "pressureday";
                public static final String COLUMN_PRESSURE_NIGHT = "pressurenight";
                public static final String COLUMN_WET = "wet";
                public static final String COLUMN_WIND_DIRECTION = "winddirection";
                public static final String COLUMN_WIND_SPEED = "windspeed";
                public static final String COLUMN_RAIN_CHANCE = "rainchance";
                public static final String COLUMN_IMG_SRC = "imgsrc";
            }
        }
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.Weather.TABLE + " (" +
                Tables.Weather.Columnes.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Tables.Weather.Columnes.COLUMN_WEATHER_DESCRIPTION + " TEXT, " +
                Tables.Weather.Columnes.COLUMN_TEMPERATURE + " TEXT, " +
                Tables.Weather.Columnes.COLUMN_PRESSURE + " TEXT, " +
                Tables.Weather.Columnes.COLUMN_WET + " TEXT, " +
                Tables.Weather.Columnes.COLUMN_WIND_DIRECTION + " TEXT, " +
                Tables.Weather.Columnes.COLUMN_WIND_SPEED + " TEXT, " +
                Tables.Weather.Columnes.COLUMN_RAIN_CHANCE + " TEXT, " +
                Tables.Weather.Columnes.COLUMN_IMG_SRC + " TEXT);");
        db.execSQL("CREATE TABLE " + Tables.WeatherForecast.TABLE + " (" +
                Tables.WeatherForecast.Columnes.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Tables.WeatherForecast.Columnes.COLUMN_DATE + " TEXT, " +
                Tables.WeatherForecast.Columnes.COLUMN_WEATHER_DESCRIPTION + " TEXT, " +
                Tables.WeatherForecast.Columnes.COLUMN_TEMPERATURE_DAY + " TEXT, " +
                Tables.WeatherForecast.Columnes.COLUMN_TEMPERATURE_NIGHT + " TEXT, " +
                Tables.WeatherForecast.Columnes.COLUMN_PRESSURE_DAY + " TEXT, " +
                Tables.WeatherForecast.Columnes.COLUMN_PRESSURE_NIGHT + " TEXT, " +
                Tables.WeatherForecast.Columnes.COLUMN_WET + " TEXT, " +
                Tables.WeatherForecast.Columnes.COLUMN_WIND_DIRECTION + " TEXT, " +
                Tables.WeatherForecast.Columnes.COLUMN_WIND_SPEED + " TEXT, " +
                Tables.WeatherForecast.Columnes.COLUMN_RAIN_CHANCE + " TEXT, " +
                Tables.WeatherForecast.Columnes.COLUMN_IMG_SRC + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.Weather.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.WeatherForecast.TABLE);
        onCreate(db);
    }
}
