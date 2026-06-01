package com.airline.repository;

import com.airline.model.Flight;
import com.airline.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByFlight(Flight flight);
    List<Seat> findByFlightAndSeatClass(Flight flight, String seatClass);
    Optional<Seat> findByFlightAndSeatNumber(Flight flight, String seatNumber);
}
