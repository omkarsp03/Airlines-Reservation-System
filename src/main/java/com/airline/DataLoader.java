package com.airline;

import com.airline.model.Flight;
import com.airline.model.Seat;
import com.airline.repository.FlightRepository;
import com.airline.repository.SeatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;

    public DataLoader(FlightRepository flightRepository, SeatRepository seatRepository) {
        this.flightRepository = flightRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public void run(String... args) {
        // Only load data if no flights exist yet
        if (flightRepository.count() == 0) {
            addFlight("AI101", "Air India", "Mumbai", "Delhi",
                    LocalDateTime.of(2026, 6, 10, 8, 0),
                    LocalDateTime.of(2026, 6, 10, 10, 30),
                    new BigDecimal("4500.00"), new BigDecimal("12000.00"),
                    140, 20);
            
            addFlight("6E202", "IndiGo", "Delhi", "Bangalore",
                    LocalDateTime.of(2026, 6, 10, 12, 0),
                    LocalDateTime.of(2026, 6, 10, 14, 45),
                    new BigDecimal("3800.00"), new BigDecimal("9500.00"),
                    160, 20);
            
            addFlight("SG303", "SpiceJet", "Bangalore", "Chennai",
                    LocalDateTime.of(2026, 6, 10, 16, 0),
                    LocalDateTime.of(2026, 6, 10, 17, 15),
                    new BigDecimal("2500.00"), new BigDecimal("7500.00"),
                    110, 10);
            
            addFlight("UK404", "Vistara", "Chennai", "Hyderabad",
                    LocalDateTime.of(2026, 6, 11, 9, 30),
                    LocalDateTime.of(2026, 6, 11, 10, 45),
                    new BigDecimal("3200.00"), new BigDecimal("8500.00"),
                    120, 20);
            
            addFlight("AI505", "Air India", "Hyderabad", "Kolkata",
                    LocalDateTime.of(2026, 6, 11, 14, 0),
                    LocalDateTime.of(2026, 6, 11, 16, 30),
                    new BigDecimal("5200.00"), new BigDecimal("13000.00"),
                    140, 20);
            
            addFlight("6E606", "IndiGo", "Kolkata", "Pune",
                    LocalDateTime.of(2026, 6, 11, 18, 0),
                    LocalDateTime.of(2026, 6, 11, 21, 0),
                    new BigDecimal("4800.00"), new BigDecimal("11500.00"),
                    150, 20);
            
            addFlight("SG707", "SpiceJet", "Pune", "Ahmedabad",
                    LocalDateTime.of(2026, 6, 12, 9, 0),
                    LocalDateTime.of(2026, 6, 12, 10, 30),
                    new BigDecimal("3000.00"), new BigDecimal("8000.00"),
                    120, 10);
            
            addFlight("UK808", "Vistara", "Ahmedabad", "Jaipur",
                    LocalDateTime.of(2026, 6, 12, 12, 0),
                    LocalDateTime.of(2026, 6, 12, 13, 15),
                    new BigDecimal("2800.00"), new BigDecimal("7800.00"),
                    100, 10);
            
            addFlight("AI909", "Air India", "Jaipur", "Goa",
                    LocalDateTime.of(2026, 6, 12, 15, 0),
                    LocalDateTime.of(2026, 6, 12, 17, 0),
                    new BigDecimal("4200.00"), new BigDecimal("11000.00"),
                    125, 15);
            
            addFlight("6E1010", "IndiGo", "Goa", "Mumbai",
                    LocalDateTime.of(2026, 6, 12, 18, 30),
                    LocalDateTime.of(2026, 6, 12, 20, 30),
                    new BigDecimal("3500.00"), new BigDecimal("9000.00"),
                    130, 20);

            System.out.println("Sample flights loaded successfully!");
        } else {
            System.out.println("Flights already exist in database, skipping data load.");
        }
    }

    private void addFlight(String flightNumber, String airline, String depCity, String arrCity,
                         LocalDateTime depTime, LocalDateTime arrTime, BigDecimal priceEconomy,
                         BigDecimal priceBusiness,
                         int totalEconomy, int totalBusiness) {
        Flight flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setAirline(airline);
        flight.setDepartureCity(depCity);
        flight.setArrivalCity(arrCity);
        flight.setDepartureTime(depTime);
        flight.setArrivalTime(arrTime);
        flight.setPriceEconomy(priceEconomy);
        flight.setPriceBusiness(priceBusiness);
        flight.setTotalSeatsEconomy(totalEconomy);
        flight.setTotalSeatsBusiness(totalBusiness);
        flight.setAvailableSeatsEconomy(totalEconomy);
        flight.setAvailableSeatsBusiness(totalBusiness);
        flight.setStatus("SCHEDULED");
        Flight savedFlight = flightRepository.save(flight);

        // Generate seats for economy (1-10 rows, A-F
        int economyRows = totalEconomy / 6;
        for (int row = 1; row <= economyRows; row++) {
            for (char col = 'A'; col <= 'F'; col++) {
                addSeat(savedFlight, row + String.valueOf(col), "ECONOMY");
            }
        }

        // Generate seats for business (1-5 rows, A-D)
        int businessRows = totalBusiness / 4;
        for (int row = 1; row <= businessRows; row++) {
            for (char col = 'A'; col <= 'D'; col++) {
                addSeat(savedFlight, "B" + row + String.valueOf(col), "BUSINESS");
            }
        }
    }

    private void addSeat(Flight flight, String seatNumber, String seatClass) {
        Seat seat = new Seat();
        seat.setFlight(flight);
        seat.setSeatNumber(seatNumber);
        seat.setSeatClass(seatClass);
        seat.setIsOccupied(false);
        seatRepository.save(seat);
    }
}
