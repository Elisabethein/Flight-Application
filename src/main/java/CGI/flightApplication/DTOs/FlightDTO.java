package CGI.flightApplication.DTOs;

import CGI.flightApplication.Entities.Flight;
import CGI.flightApplication.Entities.Seat;

import java.util.List;

public class FlightDTO {
    private Flight flight;
    private List<Seat> seats;

    public FlightDTO() {
    }

    public FlightDTO(Flight flight, List<Seat> seats) {
        this.flight = flight;
        this.seats = seats;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
