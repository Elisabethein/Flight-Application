package CGI.flightApplication.Components;

import CGI.flightApplication.Entities.Flight;
import CGI.flightApplication.Entities.Seat;
import CGI.flightApplication.Repositories.FlightRepository;
import CGI.flightApplication.Repositories.SeatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Transactional
public class DataInitializer implements ApplicationRunner {

    private final FlightRepository flightRepository;

    private final SeatRepository seatRepository;

    private final Random random = new Random();

    @Value("${API_KEY}")
    private String apiKey;

    @Autowired
    public DataInitializer(FlightRepository flightRepository, SeatRepository seatRepository) {
        this.flightRepository = flightRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception{
        if (flightRepository.count() == 0) {
            fetchAndStoreData();

            List<Flight> flights = flightRepository.findAll();

            for (Flight flight : flights) {
                generateSeats(flight);
            }
        }
    }
    private void fetchAndStoreData() throws JsonProcessingException {
        String url = "https://api.aviationstack.com/v1/flights?access_key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(result);
        JsonNode flights = rootNode.get("data");

        if (!flights.isArray() || flights.isEmpty()) {
            System.err.println("No flight data found.");
            return;
        }

        List<Flight> flightList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        int flightCount = Math.min(35, flights.size());

        for (int i = 0; i < flightCount; i++) {
            JsonNode flightNode = flights.get(i);

            String departure = flightNode.path("departure").path("airport").asText(null);
            String destination = flightNode.path("arrival").path("airport").asText(null);
            String departureTime = flightNode.path("departure").path("scheduled").asText(null);
            String arrivalTime = flightNode.path("arrival").path("scheduled").asText(null);

            if (departure == null || destination == null || departureTime == null || arrivalTime == null) {
                System.err.println("Skipping flight due to missing data.");
                continue;
            }

            double price = 100 + random.nextInt(400);
            ZonedDateTime departureDateTime = ZonedDateTime.parse(departureTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            ZonedDateTime arrivalDateTime = ZonedDateTime.parse(arrivalTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            Duration duration = Duration.between(departureDateTime, arrivalDateTime);

            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            String flightTime = hours + "h " + minutes + "m";

            // Assign a new departure date based on the current index
            LocalDate newDepartureDate = today.plusDays(i % 7);
            ZonedDateTime newDepartureDateTime = newDepartureDate.atTime(departureDateTime.toLocalTime()).atZone(departureDateTime.getZone());
            ZonedDateTime newArrivalDateTime = newDepartureDateTime.plus(duration);

            Flight flight = new Flight(
                    departure,
                    destination,
                    newDepartureDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    newArrivalDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    flightTime,
                    price
            );

            flightList.add(flight);
        }

        flightRepository.saveAll(flightList);
    }

    private void generateSeats(Flight flight) {
        int totalSeats = 60;
        int bookedSeats = (int) (totalSeats * 0.3);

        Set<String> bookedSeatNumbers = new HashSet<>();
        String[] seatLetters = {"A", "B", "C", "D", "E", "F"};

        while (bookedSeatNumbers.size() < bookedSeats) {
            int row = random.nextInt(10) + 1;
            String seat = row + seatLetters[random.nextInt(6)];
            bookedSeatNumbers.add(seat);
        }

        String[] seatClasses = {"Economy", "Business", "First"};
        double basePrice = flight.getPrice();

        List<Seat> seats = new ArrayList<>();

        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 6; col++) {
                String seatNumber = row + seatLetters[col - 1];
                String seatClass = row <= 2 ? seatClasses[2] : row <= 4 ? seatClasses[1] : seatClasses[0];
                double seatPrice = basePrice + (row <= 2 ? 1 : row <= 4 ? 0.5 : 0) * basePrice;
                boolean isBooked = bookedSeatNumbers.contains(seatNumber);
                boolean hasLegRoom = row <= 4 || row == 6;

                Seat seat = new Seat(seatNumber, seatClass, seatPrice, flight, isBooked, hasLegRoom);
                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
    }
}
