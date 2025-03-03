package CGI.flightApplication.Repositories;

import CGI.flightApplication.Entities.Flight;
import CGI.flightApplication.Entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findByFlight(Flight flight);
}
