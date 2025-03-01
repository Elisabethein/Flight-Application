package CGI.flightApplication.Entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String seatNumber;

    private String seatClass;

    private String seatPrice;
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    private Boolean isBooked;

    private Boolean hasLegRoom;

    public Seat() {
    }

    public Seat(UUID id, String seatNumber, String seatClass, String seatPrice, Flight flight, Boolean isBooked, Boolean hasLegRoom) {
        this.id = id;
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

    public String getSeatPrice() {
        return seatPrice;
    }

    public void setSeatPrice(String seatPrice) {
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
