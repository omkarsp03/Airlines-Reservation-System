package com.airline.controller;

import com.airline.model.Flight;
import com.airline.model.Seat;
import com.airline.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/flights")
    public String searchFlights(@RequestParam(required = false) String departure,
                             @RequestParam(required = false) String arrival,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             Model model,
                             @SessionAttribute(name = "currentUser", required = false) Object currentUser) {
        model.addAttribute("currentUser", currentUser);
        
        List<Flight> flights;
        if (departure != null && arrival != null && !departure.isEmpty() && !arrival.isEmpty()) {
            LocalDateTime dateTime = date != null ? date.atStartOfDay() : null;
            flights = flightService.searchFlights(departure, arrival, dateTime);
        } else {
            flights = flightService.getAllFlights();
        }
        
        model.addAttribute("flights", flights);
        return "flights";
    }

    @GetMapping("/seat-selection")
    public String seatSelectionPage(@RequestParam Long flightId,
                                    @RequestParam Integer passengers,
                                    @RequestParam String seatClass,
                                    Model model,
                                    @SessionAttribute(name = "currentUser", required = false) Object currentUser) {
        model.addAttribute("currentUser", currentUser);

        Flight flight = flightService.getFlightById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        List<Seat> seats = flightService.getSeatsByFlightIdAndClass(flightId, seatClass);
        
        model.addAttribute("flight", flight);
        model.addAttribute("passengers", passengers);
        model.addAttribute("seatClass", seatClass);
        model.addAttribute("seats", seats);
        return "seat-selection";
    }

    @GetMapping("/api/seats/{flightId}")
    @ResponseBody
    public Map<String, Object> getSeats(@PathVariable Long flightId) {
        Map<String, Object> result = new HashMap<>();
        Flight flight = flightService.getFlightById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        
        List<Seat> economySeats = flightService.getSeatsByFlightIdAndClass(flightId, "ECONOMY");
        List<Seat> businessSeats = flightService.getSeatsByFlightIdAndClass(flightId, "BUSINESS");
        
        result.put("flight", flight);
        result.put("economySeats", economySeats);
        result.put("businessSeats", businessSeats);
        return result;
    }
}
