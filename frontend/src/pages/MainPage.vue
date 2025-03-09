<template>
  <div class="main">
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
        <label for="price">Price:</label>
        <input type="number" id="price" v-model="selectedPrice" placeholder="Maximum Price" />
      </div>

      <button @click="fetchFlights">Search</button>
    </div>

    <div class="flights">
      <FlightComponent
          v-for="(flight, index) in flights"
          :key="index"
          :destination="flight.destination"
          :from="flight.departure"
          :departure="flight.departureTime"
          :arrival="flight.arrivalTime"
          :duration="flight.flightTime"
          :price="flight.price"
          :id="flight.id"
      />
    </div>
  </div>
</template>

<script>
import FlightComponent from "@/components/FlightComponent.vue";

export default {
  name: "MainPage",
  components: {FlightComponent},
  data() {
    return {
      flights: [],
      selectedFrom: "",
      selectedTo: "",
      selectedDate: "",
      selectedPrice: "",
      dropdowns: { from: false, to: false }
    };
  },
  computed: {
    uniqueDepartures() {
      return [...new Set(this.flights.map(flight => flight.departure))];
    },
    uniqueDestinations() {
      return [...new Set(this.flights.map(flight => flight.destination))];
    },
    filteredFlights() {
      return this.flights.filter(flight => {
        return (
            (!this.selectedFrom || flight.departure === this.selectedFrom) &&
            (!this.selectedTo || flight.destination === this.selectedTo) &&
            (!this.selectedDate || flight.departureTime.startsWith(this.selectedDate)) &&
            (!this.selectedPrice || flight.price <= parseFloat(this.selectedPrice))
        );
      });
    }
  },
  methods: {
    async fetchFlights() {
      try {
        const response = await fetch("http://localhost:8080/api/flights/all");

        if (response.ok) {
          this.flights = await response.json();
        } else {
          console.error("Failed to fetch flights");
        }
      } catch (error) {
        console.error("Failed to fetch flights", error);
      }
    },
    toggleDropdown(filter) {
      this.dropdowns[filter] = !this.dropdowns[filter];
    },
    selectFilter(filter, value) {
      this["selected" + filter.charAt(0).toUpperCase() + filter.slice(1)] = value;
      this.dropdowns[filter] = false;
    },
  },
  mounted() {
    this.fetchFlights();
  },
};
</script>

<style scoped>
.main {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 30px 30px 100px;
  box-sizing: border-box;
}

.filters {
  display: flex;
  justify-content: center;
  gap: 10px;
  flex-wrap: nowrap;
  padding: 10px;
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
  width: 140px;
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
  justify-content: center;
  gap: 20px;
  width: 100%;
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
</style>