package com.airline.controller;

import com.airline.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping("/cities")
    public List<String> getCitySuggestions(@RequestParam String query) {
        // Get all unique cities from flights
        List<String> allDepartureCities = flightRepository.findAll()
                .stream()
                .map(flight -> flight.getDepartureCity())
                .distinct()
                .collect(Collectors.toList());
        
        List<String> allArrivalCities = flightRepository.findAll()
                .stream()
                .map(flight -> flight.getArrivalCity())
                .distinct()
                .collect(Collectors.toList());
        
        // Combine and deduplicate
        List<String> allCities = allDepartureCities.stream()
                .distinct()
                .collect(Collectors.toList());
        allCities.addAll(allArrivalCities.stream()
                .filter(city -> !allCities.contains(city))
                .collect(Collectors.toList()));
        
        // Filter by query (case-insensitive)
        String lowerQuery = query.toLowerCase();
        return allCities.stream()
                .filter(city -> city.toLowerCase().contains(lowerQuery))
                .sorted()
                .collect(Collectors.toList());
    }
}
