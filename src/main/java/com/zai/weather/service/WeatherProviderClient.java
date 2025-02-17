package com.zai.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zai.weather.model.WeatherResponse;
import com.zai.weather.util.Base64Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class WeatherProviderClient {
    
    @Value("${weatherstack.api.url}")
    private String weatherStackUrl;

    @Value("${weatherstack.api.key}")
    private String weatherStackKey;

    @Value("${openweathermap.api.url}")
    private String openWeatherUrl;

    @Value("${openweathermap.api.key}")
    private String openWeatherKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WeatherProviderClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Optional<WeatherResponse> fetchWeatherFromWeatherStack() {
        try {
            String decodedKey = Base64Util.decode(weatherStackKey);
            String url = String.format("%s?access_key=%s&query=Melbourne", weatherStackUrl, decodedKey);
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            double temperature = jsonNode.path("current").path("temperature").asDouble();
            double windSpeed = jsonNode.path("current").path("wind_speed").asDouble();
            
            return Optional.of(new WeatherResponse(temperature, windSpeed));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<WeatherResponse> fetchWeatherFromOpenWeather() {
        try {
            String decodedKey = Base64Util.decode(openWeatherKey);
            String url = String.format("%s?q=Melbourne,AU&appid=%s&units=metric", openWeatherUrl, decodedKey);
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            double temperature = jsonNode.path("main").path("temp").asDouble();
            double windSpeed = jsonNode.path("wind").path("speed").asDouble();
            
            return Optional.of(new WeatherResponse(temperature, windSpeed));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
