package CGI.flightApplication;

import CGI.flightApplication.Entities.Flight;
import CGI.flightApplication.Exceptions.FlightNotFoundException;
import CGI.flightApplication.Services.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FlightServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlightService flightService;

    private UUID id;

    @TestConfiguration
    static class FlightServiceTestConfig {
        @Bean
        public FlightService flightService() {
            return Mockito.mock(FlightService.class);
        }
    }

    @BeforeEach
    void setUp() {
        Flight flight = new Flight("Departure", "Destination", "departureTime", "arrivalTime", "flightTime", 1.0);

        UUID generatedId = UUID.randomUUID();
        flight.setId(generatedId);
        id = generatedId;

        Mockito.when(flightService.getAllFlights()).thenReturn(java.util.List.of(flight));
        Mockito.when(flightService.getFlightById(flight.getId())).thenReturn(flight);
    }

    @Test
    public void testGetAllFlights() throws Exception {
        // Test the getAllFlights method
        mockMvc.perform(MockMvcRequestBuilders.get("/api/flights/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    // Check if the response body is not empty
                    assert !result.getResponse().getContentAsString().isEmpty();
                });
    }

    @Test
    public void testGetFlightById() throws Exception {
        // Test the getFlightById method
        mockMvc.perform(MockMvcRequestBuilders.get("/api/flights/flight/" + id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    // Check if the response body is not empty
                    assert !result.getResponse().getContentAsString().isEmpty();
                });
    }

    @Test
    public void testGetFlightByIdNotFound() throws Exception {
        // Test the getFlightById method with a non-existent ID
        UUID nonExistentId = UUID.randomUUID();

        Mockito.when(flightService.getFlightById(nonExistentId))
                .thenThrow(new FlightNotFoundException(nonExistentId));


        mockMvc.perform(MockMvcRequestBuilders.get("/api/flights/flight/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Flight with ID " + nonExistentId + " not found"));
    }
}
