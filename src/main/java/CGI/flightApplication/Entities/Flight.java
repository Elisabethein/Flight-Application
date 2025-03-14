package CGI.flightApplication.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "flights")
@Data
@Getter
@Setter
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String departure;

    private String destination;

    private String departureTime;

    private String arrivalTime;

    private String flightTime;

    private Double price;

    public Flight() {
    }

    public Flight(String departure, String destination, String departureTime, String arrivalTime, String flightTime, Double price) {
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.flightTime = flightTime;
        this.price = price;
    }

}
