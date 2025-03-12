package CGI.flightApplication;

import CGI.flightApplication.Entities.Flight;
import CGI.flightApplication.Entities.Seat;
import CGI.flightApplication.Exceptions.SeatAlreadyBookedException;
import CGI.flightApplication.Services.SeatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SeatServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SeatService seatService;

    private ObjectMapper objectMapper;
    private UUID seatId;
    private UUID bookedSeatId;


    @TestConfiguration
    static class SeatServiceTestConfig {
        @Bean
        public SeatService seatService() {
            return Mockito.mock(SeatService.class);
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        Flight flight = new Flight("Departure", "Destination", "departureTime", "arrivalTime", "flightTime", 1.0);
        Seat seat = new Seat("seatNumber", "seatClass", 500.0, flight, false, true);
        Seat bookedSeat = new Seat("seatNumber", "seatClass", 500.0, flight, true, true);

        seatId = UUID.randomUUID();
        seat.setId(seatId);

        bookedSeatId = UUID.randomUUID();
        bookedSeat.setId(bookedSeatId);

        Mockito.when(seatService.bookSeat(seatId)).thenReturn(seat);
        Mockito.when(seatService.bookSeat(bookedSeatId)).thenThrow(new SeatAlreadyBookedException("Seat with ID " + bookedSeatId + " is already booked"));
    }

    @Test
    public void testBookSeat() throws Exception {
        // Test the bookSeat method
        mockMvc.perform(MockMvcRequestBuilders.put("/api/seats/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Collections.singletonList(seatId))))
                .andExpect(status().isOk())
                .andExpect(content().string("Seat booked successfully"));
    }

    @Test
    public void testBookSeatSeatAlreadyBookedException() throws Exception {
        // Test the bookSeat method with SeatAlreadyBookedException
        mockMvc.perform(MockMvcRequestBuilders.put("/api/seats/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Collections.singletonList(bookedSeatId))))
                .andExpect(status().isConflict())
                .andExpect(content().string("Seat with ID " + bookedSeatId + " is already booked"));
    }
}
