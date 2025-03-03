package CGI.flightApplication.Services;

import CGI.flightApplication.Entities.Flight;
import CGI.flightApplication.Entities.Seat;
import CGI.flightApplication.Repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getSeatsByFlight(Flight flight) {
        return seatRepository.findByFlight(flight);
    }

    public Seat bookSeat(UUID id) {
        Seat seat = seatRepository.findById(id).orElse(null);
        if (seat != null) {
            seat.setBooked(true);
            return seatRepository.save(seat);
        }
        return null;
    }
}
