package com.airline.repository;

import com.airline.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepartureCityAndArrivalCity(String departureCity, String arrivalCity);

    @Query("SELECT f FROM Flight f WHERE f.departureCity = :departure AND f.arrivalCity = :arrival AND f.departureTime >= :date")
    List<Flight> searchFlights(@Param("departure") String departure, @Param("arrival") String arrival, @Param("date") LocalDateTime date);
}
