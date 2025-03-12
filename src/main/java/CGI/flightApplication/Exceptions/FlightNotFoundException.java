package CGI.flightApplication.Exceptions;

import java.util.UUID;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(UUID id) {
        super("Flight with ID " + id + " not found");
    }
}
