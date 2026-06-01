package com.airline.service;

import com.airline.model.Booking;
import com.airline.model.Flight;
import com.airline.model.Passenger;
import com.airline.model.Seat;
import com.airline.model.User;
import com.airline.repository.BookingRepository;
import com.airline.repository.FlightRepository;
import com.airline.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Transactional
    public Booking createBooking(User user, Flight flight, List<Passenger> passengers, String seatClass, List<String> selectedSeats) {
        // Check seats availability
        for (String seatNumber : selectedSeats) {
            Seat seat = seatRepository.findByFlightAndSeatNumber(flight, seatNumber).orElse(null);
            if (seat == null || seat.getIsOccupied()) {
                throw new RuntimeException("Seat " + seatNumber + " is not available");
            }
        }

        // Mark seats as occupied
        for (String seatNumber : selectedSeats) {
            Seat seat = seatRepository.findByFlightAndSeatNumber(flight, seatNumber).orElse(null);
            if (seat != null) {
                seat.setIsOccupied(true);
                seatRepository.save(seat);
            }
        }

        // Calculate price
        BigDecimal pricePerPerson = "ECONOMY".equals(seatClass) ? flight.getPriceEconomy() : flight.getPriceBusiness();
        BigDecimal totalAmount = pricePerPerson.multiply(BigDecimal.valueOf(passengers.size()));

        Booking booking = new Booking();
        booking.setBookingReference(generateBookingReference());
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setNumPassengers(passengers.size());
        booking.setTotalAmount(totalAmount);

        for (Passenger passenger : passengers) {
            passenger.setBooking(booking);
        }
        booking.setPassengers(passengers);

        // Update available seats count
        if ("ECONOMY".equals(seatClass)) {
            flight.setAvailableSeatsEconomy(flight.getAvailableSeatsEconomy() - passengers.size());
        } else {
            flight.setAvailableSeatsBusiness(flight.getAvailableSeatsBusiness() - passengers.size());
        }
        flightRepository.save(flight);

        return bookingRepository.save(booking);
    }

    private String generateBookingReference() {
        return "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking getBookingByReference(String reference) {
        return bookingRepository.findByBookingReference(reference);
    }
}
