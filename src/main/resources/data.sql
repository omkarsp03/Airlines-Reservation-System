-- Sample Flight Data
INSERT INTO flights (flight_number, airline, departure_city, arrival_city, departure_time, arrival_time, price, total_seats, available_seats, status)
VALUES 
('AI101', 'Air India', 'Mumbai', 'Delhi', '2026-06-10 08:00:00', '2026-06-10 10:30:00', 4500.00, 150, 145, 'SCHEDULED'),
('6E202', 'IndiGo', 'Delhi', 'Bangalore', '2026-06-10 12:00:00', '2026-06-10 14:45:00', 3800.00, 180, 170, 'SCHEDULED'),
('SG303', 'SpiceJet', 'Bangalore', 'Chennai', '2026-06-10 16:00:00', '2026-06-10 17:15:00', 2500.00, 120, 115, 'SCHEDULED'),
('UK404', 'Vistara', 'Chennai', 'Hyderabad', '2026-06-11 09:30:00', '2026-06-11 10:45:00', 3200.00, 140, 130, 'SCHEDULED'),
('AI505', 'Air India', 'Hyderabad', 'Kolkata', '2026-06-11 14:00:00', '2026-06-11 16:30:00', 5200.00, 160, 150, 'SCHEDULED'),
('6E606', 'IndiGo', 'Kolkata', 'Pune', '2026-06-11 18:00:00', '2026-06-11 21:00:00', 4800.00, 170, 160, 'SCHEDULED');
