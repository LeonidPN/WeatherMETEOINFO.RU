package com.example.weathermeteoinforu.Database;

public class WeatherModel {
    private int id;

    private String weatherDescription;

    private String temperature;

    private String pressure;

    private String wet;

    private String windDirection;

    private String windSpeed;

    private String rainChance;

    private String imgSrc;

    public WeatherModel(int id, String weatherDescription, String temperature, String pressure,
                        String wet, String windDirection, String windSpeed, String rainChance, String imgSrc) {
        this.id = id;
        this.weatherDescription = weatherDescription;
        this.temperature = temperature;
        this.pressure = pressure;
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

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
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
