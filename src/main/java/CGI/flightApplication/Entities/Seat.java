package CGI.flightApplication.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "seats")
@Data
@Getter
@Setter
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String seatNumber;

    private String seatClass;

    private Double seatPrice;
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    private Boolean isBooked;

    private Boolean hasLegRoom;

    public Seat() {
    }

    public Seat(String seatNumber, String seatClass, Double seatPrice, Flight flight, Boolean isBooked, Boolean hasLegRoom) {
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
        this.seatPrice = seatPrice;
        this.flight = flight;
        this.isBooked = isBooked;
        this.hasLegRoom = hasLegRoom;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public Double getSeatPrice() {
        return seatPrice;
    }

    public void setSeatPrice(Double seatPrice) {
        this.seatPrice = seatPrice;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Boolean getBooked() {
        return isBooked;
    }

    public void setBooked(Boolean booked) {
        isBooked = booked;
    }

    public Boolean getHasLegRoom() {
        return hasLegRoom;
    }

    public void setHasLegRoom(Boolean hasLegRoom) {
        this.hasLegRoom = hasLegRoom;
    }
}
