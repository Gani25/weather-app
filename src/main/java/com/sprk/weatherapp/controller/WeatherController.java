package com.sprk.weatherapp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {

    @Value("${apiKey}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/getweatherwithlocation")
    public String getWeatherWithLocation(@RequestParam String latitude, String longitude, Model model) {
        // return apiKey + " " + cityName;
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid="
                + apiKey;
        JsonNode apiResponse = restTemplate.getForObject(url, JsonNode.class);
        // JsonNode apiJSON;
        String cityName;
        // try {
        // apiJSON = objectMapper.readTree(apiResponse);
        cityName = apiResponse.path("name").asText();
        double tempKelvin = apiResponse.path("main").path("temp").asDouble();
        // System.out.println(tempKelvin);
        double tempCelcius = tempKelvin - 273.15;
        String celcius = String.format("%.2f", tempCelcius);
        tempCelcius = Double.parseDouble(celcius);
        // System.out.println(tempCelcius);

        int humidity = apiResponse.path("main").path("humidity").asInt();
        System.out.println(humidity);

        double windSpeed = apiResponse.path("wind").path("speed").asDouble();
        System.out.println(windSpeed);

        // long dateNum = apiResponse.path("dt").asLong();
        Date currentDate = new Date();

        System.out.println(currentDate);

        String cloudCOndition = apiResponse.path("weather").get(0).path("main").asText();
        System.out.println(cloudCOndition);
        model.addAttribute("cityName", cityName);
        model.addAttribute("humidity", humidity);
        model.addAttribute("cloudCondition", cloudCOndition);
        model.addAttribute("windSpeed", windSpeed);
        model.addAttribute("date", currentDate);
        model.addAttribute("temp", tempCelcius);

        return "dashboard";
        // } catch (Exception e) {
        // // TODO: handle exception
        // return null;
        // }
    }

    @PostMapping("/getweather")
    public String getCurrentTempString(@RequestParam String cityName, Model model) {
        // return apiKey + " " + cityName;
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName
                + "&appid=" + apiKey;

        JsonNode apiResponse = restTemplate.getForObject(url, JsonNode.class);
        // JsonNode apiJSON;
        // try {
        // apiJSON = objectMapper.readTree(apiResponse);
        double tempKelvin = apiResponse.path("main").path("temp").asDouble();
        // System.out.println(tempKelvin);
        double tempCelcius = tempKelvin - 273.15;
        String celcius = String.format("%.2f", tempCelcius);
        tempCelcius = Double.parseDouble(celcius);
        // System.out.println(tempCelcius);

        int humidity = apiResponse.path("main").path("humidity").asInt();
        System.out.println(humidity);

        double windSpeed = apiResponse.path("wind").path("speed").asDouble();
        System.out.println(windSpeed);

        // long dateNum = apiResponse.path("dt").asLong();
        LocalDateTime currentDateWithTime = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

        String currentDate = currentDateWithTime.format(myFormatObj);
        System.out.println(currentDate);

        String cloudCOndition = apiResponse.path("weather").get(0).path("main").asText();
        System.out.println(cloudCOndition);
        model.addAttribute("cityName", cityName);
        model.addAttribute("humidity", humidity);
        model.addAttribute("cloudCondition", cloudCOndition);
        model.addAttribute("windSpeed", windSpeed);
        model.addAttribute("date", currentDate);
        model.addAttribute("temp", tempCelcius);

        return "dashboard";
        // } catch (Exception e) {
        // // TODO: handle exception
        // return null;
        // }
    }

}
