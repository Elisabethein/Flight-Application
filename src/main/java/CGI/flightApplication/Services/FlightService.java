package CGI.flightApplication.Services;

import CGI.flightApplication.Entities.Flight;
import CGI.flightApplication.Exceptions.FlightNotFoundException;
import CGI.flightApplication.Repositories.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Flight getFlightById(UUID id) {
        return flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException(id));
    }
}
