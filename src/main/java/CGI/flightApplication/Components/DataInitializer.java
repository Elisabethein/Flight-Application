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
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final String URL = "https://api.aviationstack.com/v1/flights?access_key=";
    private final int MAX_FLIGHTS = 35;
    private final int BASE_PRICE = 100;
    private final int ADDITIONAL_PRICE = 400;
    private final int MAX_SEATS = 60;
    private final String[] seatLetters = {"A", "B", "C", "D", "E", "F"};
    private final String[] seatClasses = {"Economy", "Business", "First"};
    private final Random random = new Random();

    @Value("${API_KEY}")
    private String apiKey;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Initialize the database with flight data if it is empty
        if (flightRepository.count() == 0) {
            fetchAndStoreData();
            flightRepository.findAll().forEach(this::generateSeats);
        }
    }

    private void fetchAndStoreData() throws JsonProcessingException {
        JsonNode flights = fetchFlightData();
        if (flights == null || !flights.isArray() || flights.isEmpty()) {
            System.err.println("No flight data found.");
            return;
        }

        List<Flight> flightList = processFlightData(flights);
        addTestFlights(flightList);
        flightRepository.saveAll(flightList);
    }
    private JsonNode fetchFlightData() throws JsonProcessingException {
        String url = URL + apiKey;
        String result = new RestTemplate().getForObject(url, String.class);
        return new ObjectMapper().readTree(result).get("data");
    }

    private List<Flight> processFlightData(JsonNode flights) {
        List<Flight> flightList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int flightCount = Math.min(MAX_FLIGHTS, flights.size());

        for (int i = 0; i < flightCount; i++) {
            Flight flight = extractFlightData(flights.get(i), today.plusDays(i % 7));
            if (flight != null) flightList.add(flight);
        }
        return flightList;
    }

    private Flight extractFlightData(JsonNode flightNode, LocalDate departureDate) {
        String departure = flightNode.path("departure").path("airport").asText(null);
        String destination = flightNode.path("arrival").path("airport").asText(null);
        String departureTime = flightNode.path("departure").path("scheduled").asText(null);
        String arrivalTime = flightNode.path("arrival").path("scheduled").asText(null);

        if (departure == null || destination == null || departureTime == null || arrivalTime == null) {
            System.err.println("Skipping flight due to missing data.");
            return null;
        }

        double price = BASE_PRICE + random.nextInt(ADDITIONAL_PRICE);
        ZonedDateTime departureDateTime = ZonedDateTime.parse(departureTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        ZonedDateTime arrivalDateTime = ZonedDateTime.parse(arrivalTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        Duration duration = Duration.between(departureDateTime, arrivalDateTime);
        String flightTime = duration.toHours() + "h " + (duration.toMinutes() % 60) + "m";

        ZonedDateTime newDepartureDateTime = departureDate.atTime(departureDateTime.toLocalTime()).atZone(departureDateTime.getZone());
        ZonedDateTime newArrivalDateTime = newDepartureDateTime.plus(duration);

        return new Flight(departure, destination, newDepartureDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                newArrivalDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), flightTime, price);
    }

    private void addTestFlights(List<Flight> flightList) {
        flightList.add(new Flight("JFK", "LAX", "2025-03-11T07:00:00Z", "2025-03-11T10:40:00Z", "3h 40m", 200.0));
        flightList.add(new Flight("LAX", "SFO", "2025-03-12T02:30:00Z", "2025-03-12T08:00:00Z", "5h 30m", 100.0));
    }

    private void generateSeats(Flight flight) {
        Set<String> bookedSeatNumbers = generateBookedSeats();
        List<Seat> seats = createSeats(flight, bookedSeatNumbers);
        seatRepository.saveAll(seats);
    }

    private Set<String> generateBookedSeats() {
        Set<String> bookedSeatNumbers = new HashSet<>();

        while (bookedSeatNumbers.size() < MAX_SEATS * 0.3) {
            int row = random.nextInt(10) + 1;
            String seat = row + seatLetters[random.nextInt(6)];
            bookedSeatNumbers.add(seat);
        }
        return bookedSeatNumbers;
    }

    private List<Seat> createSeats(Flight flight, Set<String> bookedSeatNumbers) {
        List<Seat> seats = new ArrayList<>();
        double basePrice = flight.getPrice();

        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 6; col++) {
                String seatNumber = row + seatLetters[col - 1];
                String seatClass = row <= 2 ? seatClasses[2] : row <= 4 ? seatClasses[1] : seatClasses[0];
                double seatPrice = basePrice * (row <= 2 ? 2 : row <= 4 ? 1.5 : 1);
                boolean isBooked = bookedSeatNumbers.contains(seatNumber);
                boolean hasLegRoom = row <= 4 || row == 6;

                seats.add(new Seat(seatNumber, seatClass, seatPrice, flight, isBooked, hasLegRoom));
            }
        }
        return seats;
    }
}
