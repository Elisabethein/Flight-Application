package CGI.flightApplication.Services;

import CGI.flightApplication.Entities.Flight;
import CGI.flightApplication.Entities.Seat;
import CGI.flightApplication.Exceptions.SeatAlreadyBookedException;
import CGI.flightApplication.Repositories.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    public List<Seat> getSeatsByFlight(Flight flight) {
        return seatRepository.findByFlight(flight);
    }

    public Seat bookSeat(UUID id) {
        Seat seat = seatRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Seat not found")
        );

        if (Boolean.TRUE.equals(seat.getBooked())) {
            throw new SeatAlreadyBookedException("Seat with ID " + id + " is already booked");
        }

        seat.setBooked(true);
        return seatRepository.save(seat);
    }
}
