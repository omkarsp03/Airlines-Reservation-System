package com.airline.controller;

import com.airline.model.Booking;
import com.airline.model.Flight;
import com.airline.model.Passenger;
import com.airline.model.User;
import com.airline.service.BookingService;
import com.airline.service.FlightService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private FlightService flightService;

    @GetMapping("/booking")
    public String bookingPage(@RequestParam Long flightId,
                              @RequestParam Integer passengers,
                              @RequestParam String seatClass,
                              @RequestParam List<String> selectedSeats,
                              Model model,
                              HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Flight flight = flightService.getFlightById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        
        // Calculate prices
        java.math.BigDecimal pricePerPerson = "ECONOMY".equals(seatClass) ? flight.getPriceEconomy() : flight.getPriceBusiness();
        java.math.BigDecimal totalPrice = pricePerPerson.multiply(java.math.BigDecimal.valueOf(passengers));
        
        model.addAttribute("flight", flight);
        model.addAttribute("passengers", passengers);
        model.addAttribute("seatClass", seatClass);
        model.addAttribute("selectedSeats", selectedSeats);
        model.addAttribute("pricePerPerson", pricePerPerson);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("currentUser", currentUser);
        return "booking";
    }

    @PostMapping("/booking")
    public String createBooking(@RequestParam Long flightId,
                                @RequestParam List<String> passengerNames,
                                @RequestParam List<Integer> passengerAges,
                                @RequestParam List<String> passengerGenders,
                                @RequestParam List<String> selectedSeats,
                                @RequestParam String seatClass,
                                HttpSession session,
                                Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Flight flight = flightService.getFlightById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < passengerNames.size(); i++) {
            Passenger p = new Passenger();
            p.setFullName(passengerNames.get(i));
            p.setAge(passengerAges.get(i));
            p.setGender(passengerGenders.get(i));
            p.setSeatNumber(selectedSeats.get(i));
            passengers.add(p);
        }

        Booking booking = bookingService.createBooking(currentUser, flight, passengers, seatClass, selectedSeats);
        model.addAttribute("booking", booking);
        model.addAttribute("currentUser", currentUser);
        return "booking-confirmation";
    }

    @GetMapping("/my-bookings")
    public String myBookings(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Booking> bookings = bookingService.getBookingsByUser(currentUser.getId());
        model.addAttribute("bookings", bookings);
        model.addAttribute("currentUser", currentUser);
        return "my-bookings";
    }
}
