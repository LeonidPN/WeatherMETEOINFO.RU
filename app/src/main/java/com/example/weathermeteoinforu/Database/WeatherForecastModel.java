package com.example.weathermeteoinforu.Database;

public class WeatherForecastModel {
    private int id;

    private String date;

    private String weatherDescription;

    private String temperatureDay;

    private String temperatureNight;

    private String pressureDay;

    private String pressureNight;

    private String wet;

    private String windDirection;

    private String windSpeed;

    private String rainChance;

    private String imgSrc;

    public WeatherForecastModel(int id, String date, String weatherDescription, String temperatureDay,
                                String temperatureNight, String pressureDay, String pressureNight,
                                String wet, String windDirection, String windSpeed, String rainChance,
                                String imgSrc) {
        this.id = id;
        this.date = date;
        this.weatherDescription = weatherDescription;
        this.temperatureDay = temperatureDay;
        this.temperatureNight = temperatureNight;
        this.pressureDay = pressureDay;
        this.pressureNight = pressureNight;
        this.wet = wet;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.rainChance = rainChance;
        this.imgSrc = imgSrc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getTemperatureDay() {
        return temperatureDay;
    }

    public void setTemperatureDay(String temperatureDay) {
        this.temperatureDay = temperatureDay;
    }

    public String getTemperatureNight() {
        return temperatureNight;
    }

    public void setTemperatureNight(String temperatureNight) {
        this.temperatureNight = temperatureNight;
    }

    public String getPressureDay() {
        return pressureDay;
    }

    public void setPressureDay(String pressureDay) {
        this.pressureDay = pressureDay;
    }

    public String getPressureNight() {
        return pressureNight;
    }

    public void setPressureNight(String pressureNight) {
        this.pressureNight = pressureNight;
    }

    public String getWet() {
        return wet;
    }

    public void setWet(String wet) {
        this.wet = wet;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getRainChance() {
        return rainChance;
    }

    public void setRainChance(String rainChance) {
        this.rainChance = rainChance;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
