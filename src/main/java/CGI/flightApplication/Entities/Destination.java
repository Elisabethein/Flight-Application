package CGI.flightApplication.Entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "destinations")
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String destinationName;

    private String destinationCountry;

    public Destination() {
    }

    public Destination(UUID id, String destinationName, String destinationCountry) {
        this.id = id;
        this.destinationName = destinationName;
        this.destinationCountry = destinationCountry;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }
}
