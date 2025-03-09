package CGI.flightApplication.Controllers;

import CGI.flightApplication.Entities.Seat;
import CGI.flightApplication.Services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<String> bookSeat(@PathVariable UUID id) {
        try {
            seatService.bookSeat(id);
            return ResponseEntity.ok("Seat booked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
