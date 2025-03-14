package CGI.flightApplication.DTOs;

import CGI.flightApplication.Entities.Flight;
import CGI.flightApplication.Entities.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class FlightDTO {
    private Flight flight;
    private List<Seat> seats;

    public FlightDTO() {
    }
}
