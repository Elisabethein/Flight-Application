package CGI.flightApplication.Controllers;

import CGI.flightApplication.Services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PutMapping("/book")
    public ResponseEntity<String> bookSeat(@RequestBody List<UUID> seatIds) {
        for (UUID seatId : seatIds) {
            seatService.bookSeat(seatId);
        }
        return ResponseEntity.ok("Seat booked successfully");
    }
}
