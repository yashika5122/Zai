package com.zai.weather.service;

import com.zai.weather.model.WeatherResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    
    private final WeatherProviderClient weatherProviderClient;

    public WeatherService(WeatherProviderClient weatherProviderClient) {
        this.weatherProviderClient = weatherProviderClient;
    }

    @Cacheable(value = "weatherCache")
    public WeatherResponse getWeather() {
        System.out.println("get weather service:");
        return weatherProviderClient.fetchWeatherFromWeatherStack()
                .or(weatherProviderClient::fetchWeatherFromOpenWeather)
                .orElseThrow(() -> new RuntimeException("All weather providers are down."));
    }
}
