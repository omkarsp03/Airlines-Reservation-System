package com.airline.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", unique = true, nullable = false, length = 10)
    private String flightNumber;

    @Column(nullable = false, length = 100)
    private String airline;

    @Column(name = "departure_city", nullable = false, length = 50)
    private String departureCity;

    @Column(name = "arrival_city", nullable = false, length = 50)
    private String arrivalCity;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "price_economy", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceEconomy;

    @Column(name = "price_business", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceBusiness;

    @Column(name = "total_seats_economy", nullable = false)
    private Integer totalSeatsEconomy;

    @Column(name = "total_seats_business", nullable = false)
    private Integer totalSeatsBusiness;

    @Column(name = "available_seats_economy", nullable = false)
    private Integer availableSeatsEconomy;

    @Column(name = "available_seats_business", nullable = false)
    private Integer availableSeatsBusiness;

    @Column(length = 20)
    private String status = "SCHEDULED";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public BigDecimal getPriceEconomy() {
        return priceEconomy;
    }

    public void setPriceEconomy(BigDecimal priceEconomy) {
        this.priceEconomy = priceEconomy;
    }

    public BigDecimal getPriceBusiness() {
        return priceBusiness;
    }

    public void setPriceBusiness(BigDecimal priceBusiness) {
        this.priceBusiness = priceBusiness;
    }

    public Integer getTotalSeatsEconomy() {
        return totalSeatsEconomy;
    }

    public void setTotalSeatsEconomy(Integer totalSeatsEconomy) {
        this.totalSeatsEconomy = totalSeatsEconomy;
    }

    public Integer getTotalSeatsBusiness() {
        return totalSeatsBusiness;
    }

    public void setTotalSeatsBusiness(Integer totalSeatsBusiness) {
        this.totalSeatsBusiness = totalSeatsBusiness;
    }

    public Integer getAvailableSeatsEconomy() {
        return availableSeatsEconomy;
    }

    public void setAvailableSeatsEconomy(Integer availableSeatsEconomy) {
        this.availableSeatsEconomy = availableSeatsEconomy;
    }

    public Integer getAvailableSeatsBusiness() {
        return availableSeatsBusiness;
    }

    public void setAvailableSeatsBusiness(Integer availableSeatsBusiness) {
        this.availableSeatsBusiness = availableSeatsBusiness;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
