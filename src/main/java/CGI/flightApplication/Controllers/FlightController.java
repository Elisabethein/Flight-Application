package CGI.flightApplication.Controllers;

import CGI.flightApplication.DTOs.FlightDTO;
import CGI.flightApplication.Entities.Flight;
import CGI.flightApplication.Entities.Seat;
import CGI.flightApplication.Services.FlightService;
import CGI.flightApplication.Services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    private final SeatService seatService;

    @Autowired
    public FlightController(FlightService flightService, SeatService seatService) {
        this.flightService = flightService;
        this.seatService = seatService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Flight>> getAllFlights() {
        try {
            return ResponseEntity.ok(flightService.getAllFlights());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/flight/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable UUID id) {
        try {
            Flight flight = flightService.getFlightById(id);
            List<Seat> seats = seatService.getSeatsByFlight(flight);

            FlightDTO flightDTO = new FlightDTO(flight, seats);

            return ResponseEntity.ok(flightDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }

    }
}
