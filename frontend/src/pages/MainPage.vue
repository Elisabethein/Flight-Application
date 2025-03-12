<template>
  <div class="body">
  <div class="main">
    <div class="welcome">
      <h1>Find the Best Flights for Your Journey!</h1>


    <div class="filters">
      <div class="filter" @click="toggleDropdown('from')">
        <label for="from">From:</label>
        <div class="dropdown" v-if="dropdowns.from">
          <div v-for="departure in uniqueDepartures" :key="departure" @click.stop="selectFilter('from', departure)">
            {{ departure }}
          </div>
        </div>
        <div class="selected">{{ selectedFrom || 'Select Departure' }}</div>
      </div>

      <div class="filter" @click="toggleDropdown('to')">
        <label for="to">To:</label>
        <div class="dropdown" v-if="dropdowns.to">
          <div v-for="destination in uniqueDestinations" :key="destination" @click.stop="selectFilter('to', destination)">
            {{ destination }}
          </div>
        </div>
        <div class="selected">{{ selectedTo || 'Select Destination' }}</div>
      </div>

      <div class="filter">
        <label for="date">Date:</label>
        <input type="date" id="date" v-model="selectedDate" />
      </div>

      <div class="filter">
        <label for="time">Time:</label>
        <input type="time" id="time" v-model="selectedTime" />
      </div>

      <div class="filter">
        <label for="price">Price:</label>
        <input type="number" id="price" v-model="selectedPrice" placeholder="Maximum Price" />
      </div>

      <button class="src-button" @click="filterFlights">Search</button>
    </div>

  </div>

    <div class="flights">
      <div class="flight-row" v-for="(pair, index) in flightPairs" :key="index">
        <template v-if="pair.length === 2">
          <ConnectingFlightComponent
              :firstFlight="pair[0]"
              :secondFlight="pair[1]"
          />
        </template>
        <template v-else>
          <FlightComponent
              :destination="pair[0].destination"
              :from="pair[0].departure"
              :departure="pair[0].departureTime"
              :arrival="pair[0].arrivalTime"
              :duration="pair[0].flightTime"
              :price="pair[0].price"
              :id="pair[0].id"
          />
        </template>
      </div>
    </div>

    <div class="notification">
      <p v-if="!filteredFlights.length">No flights found</p>
    </div>

    <div class="pagination">
      <button
          v-for="page in totalPages"
          :key="page"
          :class="{ active: currentPage === page }"
          @click="goToPage(page)"
      >
        {{ page }}
      </button>
    </div>
  </div>
  </div>
</template>

<script>
import FlightComponent from "@/components/FlightComponent.vue";
import ConnectingFlightComponent from "@/components/ConnectingFlightComponent.vue";

export default {
  name: "MainPage",
  components: {
    FlightComponent,
    ConnectingFlightComponent
  },
  data() {
    return {
      flights: [],
      currentPage: 1,
      flightsPerPage: 6,
      selectedFrom: "",
      selectedTo: "",
      selectedDate: "",
      selectedTime: "",
      selectedPrice: "",
      dropdowns: { from: false, to: false },
      filteredFlights: [],
    };
  },
  computed: {
    uniqueDepartures() {
      return [...new Set(this.flights.map(flight => flight.departure))];
    },
    uniqueDestinations() {
      return [...new Set(this.flights.map(flight => flight.destination))];
    },
    paginatedFlights() {
      const startIndex = (this.currentPage - 1) * this.flightsPerPage;
      return this.filteredFlights.slice(startIndex, startIndex + this.flightsPerPage);
    },
    totalPages() {
      return Math.ceil(this.filteredFlights.length / this.flightsPerPage);
    },
    flightPairs() {
      return this.paginatedFlights.map(flight => {
        if (Array.isArray(flight)) {
          return flight; // Keep flight pairs as they are
        } else {
          return [flight]; // Wrap single flights in an array for consistent structure
        }
      });
    }
  },
  methods: {
    async fetchFlights() {
      try {
        const response = await fetch("http://localhost:8080/api/flights/all");

        if (response.ok) {
          this.flights = await response.json();

          // sorting ascendingly based on the departure time
          this.flights.sort((a, b) => a.departureTime.localeCompare(b.departureTime));

          this.filteredFlights = [...this.flights];

        } else {
          console.error("Failed to fetch flights");
        }
      } catch (error) {
        console.error("Failed to fetch flights", error);
      }
    },
    filterFlights() {
      this.currentPage = 1;

      // Filter flights by departure, destination, date, and price
      let filtered = this.flights.filter((flight) => {
        const matchesDeparture = this.selectedFrom ? flight.departure === this.selectedFrom : true;
        const matchesDestination = this.selectedTo ? flight.destination === this.selectedTo : true;
        const matchesPrice = this.selectedPrice ? flight.price <= parseFloat(this.selectedPrice) : true;
        const matchesDate = this.selectedDate ? flight.departureTime.startsWith(this.selectedDate) : true;

        const flightTime = new Date(flight.departureTime);
        const flightTimeString = `${flightTime.getHours().toString().padStart(2, '0')}:${flightTime.getMinutes().toString().padStart(2, '0')}`;
        const matchesTime = this.selectedTime ? flightTimeString === this.selectedTime : true;

        return matchesDeparture && matchesDestination && matchesPrice && matchesDate && matchesTime
      });

      // Step 2: If no results are found, search for two connecting flights

      if (filtered.length === 0) {
        filtered = [];

        this.flights.forEach((firstFlight) => {
          if (firstFlight.departure === this.selectedFrom) {
            const possibleConnections = this.flights.filter(
                (secondFlight) =>
                    secondFlight.departure === firstFlight.destination &&
                    secondFlight.destination === this.selectedTo &&
                    secondFlight.departureTime > firstFlight.arrivalTime &&
                    (this.isSameDay(firstFlight.arrivalTime, secondFlight.departureTime) || this.isPreviousDay(secondFlight.departureTime, firstFlight.arrivalTime)) &&
                    (this.selectedDate ? secondFlight.departureTime.startsWith(this.selectedDate) : true)
            );
            possibleConnections.forEach((secondFlight) => {
              filtered.push([firstFlight, secondFlight]);
            });
          }
        });
      }

      this.filteredFlights = filtered;
    },
    isSameDay(firstDate, secondDate) {
      const first = new Date(firstDate);
      const second = new Date(secondDate);
      return first.toDateString() === second.toDateString();
    },
    isPreviousDay(firstDate, secondDate) {
      const first = new Date(firstDate);
      const second = new Date(secondDate);
      return second.getDate() === first.getDate() - 1;
    },
    toggleDropdown(filter) {
      this.dropdowns[filter] = !this.dropdowns[filter];
    },
    selectFilter(filter, value) {
      this["selected" + filter.charAt(0).toUpperCase() + filter.slice(1)] = value;
      this.dropdowns[filter] = false;
    },
    goToPage(page) {
      if (page < 1 || page > this.totalPages) return; // Prevent going out of bounds
      this.currentPage = page;
    },
  },
  mounted() {
    this.fetchFlights();
  },
};
</script>

<style scoped>
.body {
  height: 100%;
  margin: 0;
  padding: 0;
  background: url("../assets/gradient.png");
  background-size: cover;
  border-radius: 20px;
  min-height: 100vh;
}

.main {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 30px 30px 100px;
  box-sizing: border-box;
  margin-top: 5px;

}

.filters {
  display: flex;
  justify-content: center;
  gap: 10px;
  flex-wrap: wrap;
  padding: 10px;
  align-items: center;
}

.filter {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fef2ea;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 4px 6px -2px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  width: 200px;
}

.selected {
  padding: 5px;
  background: white;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 100%;
  text-align: center;
}

.dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  background: white;
  border: 1px solid #ccc;
  border-radius: 5px;
  max-height: 150px;
  overflow-y: auto;
  display: flex;
  flex-wrap: wrap;
  padding: 5px;
}

.dropdown div {
  padding: 5px;
  width: 100%;
  cursor: pointer;
  white-space: nowrap;
}

.dropdown div:hover {
  background: #ff7f50;
  color: white;
}

input {
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 100%;
}

.flights {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  width: 100%;
}

.flight-row {
  gap: 20px;
  width: calc(50% - 10px);
  justify-content: center;
}

.flight-row > * {
  flex: 1; /* Ensures equal width */
}

button {
  padding: 5px 10px;
  margin: 15px 5px;
  border: none;
  background-color: #ff7f50;
  color: white;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #c7562b;
}

.src-button {
  padding: 12px 24px;
  font-size: 18px;
  background-color: #ff7f50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease-in-out;
}

.src-button:hover {
  background-color: #c7562b;
}
</style>