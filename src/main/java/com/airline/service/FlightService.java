package com.airline.service;

import com.airline.model.Flight;
import com.airline.model.Seat;
import com.airline.repository.FlightRepository;
import com.airline.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;

    public FlightService(FlightRepository flightRepository, SeatRepository seatRepository) {
        this.flightRepository = flightRepository;
        this.seatRepository = seatRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public List<Flight> searchFlights(String departureCity, String arrivalCity, LocalDateTime departureDate) {
        return flightRepository.findByDepartureCityAndArrivalCity(departureCity, arrivalCity);
    }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public List<Seat> getSeatsByFlightId(Long flightId) {
        return seatRepository.findByFlight(flightRepository.findById(flightId).orElse(null));
    }

    public List<Seat> getSeatsByFlightIdAndClass(Long flightId, String seatClass) {
        return seatRepository.findByFlightAndSeatClass(flightRepository.findById(flightId).orElse(null), seatClass);
    }

    public Optional<Seat> getSeatByFlightIdAndNumber(Long flightId, String seatNumber) {
        return seatRepository.findByFlightAndSeatNumber(flightRepository.findById(flightId).orElse(null), seatNumber);
    }

    public boolean bookSeat(Long flightId, String seatNumber) {
        Optional<Seat> seatOpt = getSeatByFlightIdAndNumber(flightId, seatNumber);
        if (seatOpt.isPresent()) {
            Seat seat = seatOpt.get();
            if (!seat.getIsOccupied()) {
                seat.setIsOccupied(true);
                seatRepository.save(seat);
                return true;
            }
        }
        return false;
    }
}
