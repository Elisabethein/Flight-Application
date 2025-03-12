<template>
  <div class="body">
    <div class="back">
      <button  @click="$router.push('/')">← Back</button>
    </div>

    <div class="flight">
        <FlightComponent :key="flight.id"
                         :destination="flight.destination"
                         :from="flight.departure"
                         :departure="flight.departureTime"
                         :arrival="flight.arrivalTime"
                         :duration="flight.flightTime"
                         :price="flight.price"
                         :disableClick="true" />
    </div>

    <div class="filters">

      <div class="filter">
        <label for="class">Class:</label>
        <select id="class" v-model="filters.class">
          <option value="Economy">Economy</option>
          <option value="Business">Business</option>
          <option value="First Class">First Class</option>
        </select>
      </div>

      <div class="filter">
        <label for="withLegRoom">Extra leg room:</label>
        <input type="checkbox" id="withLegRoom" v-model="filters.withLegRoom" />
      </div>

      <div class="filter">
        <label for="windowSeat">Window seat:</label>
        <input type="checkbox" id="windowSeat" v-model="filters.windowSeat"/>
      </div>

      <div class="filter">
        <label for="nearExit">Near exit:</label>
        <input type="checkbox" id="nearExit" v-model="filters.nearExit" />
      </div>

      <div class="filter">
        <label for="nextToEachOther">Next to each other:</label>
        <input type="checkbox" id="nextToEachOther" v-model="filters.nextToEachOther"/>
      </div>

      <div class="filter">
        <label for="number">How many seats:</label>
        <div class="seat-selector">
          <button @click="decreaseSeats" :disabled="filters.number <= 1">-</button>
          <span>{{ filters.number }}</span>
          <button @click="increaseSeats" :disabled="filters.number >= 10">+</button>
        </div>
      </div>

      <button class="src-button" @click="showSeats">Search</button>


    </div>

    <div class="seatData">
      <div class="seat-plan">
        <SeatPlanComponent ref="seatPlanComponent" :seats="seatPlan" @update-selected-seats="updateSelectedSeats" />
      </div>

      <div class="seat-info">
        <div class="selected-seats">
          <h3>Selected seats:</h3>
          <ul>
            <li v-for="seat in selectedSeats" :key="seat.seatNumber">
              <strong>{{ seat.seatNumber }} ({{ seat.seatClass }} Class) :</strong> {{ seat.seatPrice }} €
            </li>
          </ul>
        </div>
        <div class="total-price">
          <h3>Total price: {{ totalPrice }} €</h3>
          <div class="button-container">
            <button @click="confirmBooking">Book Seats</button>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<script>
import { mapState } from "vuex";
import FlightComponent from "@/components/FlightComponent.vue";
import SeatPlanComponent from "@/components/SeatPlanComponent.vue";

export default {
  name: "FlightPage",
  components: {
    FlightComponent,
    SeatPlanComponent,
  },
  data() {
    return {
      filters: {
        class: "Economy",
        withLegRoom: false,
        nearExit: false,
        windowSeat: false,
        nextToEachOther: false,
        number: 1,
      },
      selectedSeats: [],
    };
  },
  computed: {
    ...mapState(["flight", "seatPlan"]),
    totalPrice() {
      return this.selectedSeats.reduce((sum, seat) => sum + seat.seatPrice, 0);
    }
  },
  methods: {
    increaseSeats() {
      if (this.filters.number < 10) {
        this.filters.number++;
      }
    },
    decreaseSeats() {
      if (this.filters.number > 1) {
        this.filters.number--;
      }
    },
    showSeats() {
      if (!this.seatPlan || this.seatPlan.length === 0) return;

      let availableSeats = this.seatPlan.filter(
          (seat) => seat.seatClass === this.filters.class && !seat.booked
      );

      availableSeats.forEach(seat => {
        seat.score = 0;

        // Extra legroom (Binary: 10 or 0)
        if (this.filters.withLegRoom) {
          seat.score += seat.hasLegRoom ? 10 : 0;
        }

        // Near exit (Scored based on proximity)
        if (this.filters.nearExit) {
          const row = parseInt(seat.seatNumber.match(/(\d+)([A-F])/)?.slice(1)[0]);
          if ([1, 6, 10].includes(row)) {
            seat.score += 10;
          } else if ([2, 5, 7, 9].includes(row)) {
            seat.score += 5;
          } else if ([3, 4, 8].includes(row)) {
            seat.score += 1;
          }
        }

        // Window seat (Weighted score)
        if (this.filters.windowSeat) {
          const col = seat.seatNumber.match(/(\d+)([A-F])/)?.slice(1)[1];
          if (['A', 'F'].includes(col)) {
            seat.score += 10;
          } else if (['B', 'E'].includes(col)) {
            seat.score += 5;
          } else if (['C', 'D'].includes(col)) {
            seat.score += 1;
          }
        }
      });

      // Sort seats by score in descending order
      availableSeats.sort((a, b) => b.score - a.score);

      let selectedSeats = [];
      if (this.filters.nextToEachOther && this.filters.number > 1) {
        for (let i = 0; i < availableSeats.length; i++) {
          let group = [availableSeats[i]];
          for (let j = i + 1; j < availableSeats.length; j++) {
            if (group.length >= this.filters.number) break;
            if (this.areSeatsAdjacent(group[group.length - 1], availableSeats[j])) {
              group.push(availableSeats[j]);
            }
          }
          if (group.length === this.filters.number) {
            selectedSeats = group;
            break;
          }
        }
      }

      if (selectedSeats.length === 0) {
        selectedSeats = availableSeats.slice(0, this.filters.number);
      }

      this.selectedSeats = selectedSeats;
      this.$refs.seatPlanComponent.selectSeatsFromFilter(this.selectedSeats);
    },
    areSeatsAdjacent(seat1, seat2) {
      const row1 = parseInt(seat1.seatNumber.match(/(\d+)/)[0]);
      const row2 = parseInt(seat2.seatNumber.match(/(\d+)/)[0]);
      const col1 = seat1.seatNumber.match(/[A-F]/)[0];
      const col2 = seat2.seatNumber.match(/[A-F]/)[0];

      const adjacentColumns = {
        'A': 'B', 'B': 'A,C', 'C': 'B,D',
        'D': 'C,E', 'E': 'D,F', 'F': 'E'
      };

      return row1 === row2 && adjacentColumns[col1].includes(col2);
    },
    updateSelectedSeats(seats) {
      this.selectedSeats = seats;
    },
    confirmBooking() {
      if (this.selectedSeats.length === 0) {
        alert("Please select seats first");
        return;
      }
      if (!confirm("Are you sure you want to book these seats?")) return;

      this.bookSeats();
    },
    async bookSeats() {
      try {
        const response = await fetch("http://localhost:8080/api/seats/book", {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(this.selectedSeats.map(seat => seat.id)),
        });

        if (!response.ok) {
          if (response.status === 409) {
            alert("One or more seats have already been booked!");
          } else {
            throw new Error("Failed to book seats");
          }
        }

        alert(
            "Seats " +
            this.selectedSeats.map((seat) => seat.seatNumber).join(", ") +
            " booked successfully!"
        );

        this.selectedSeats = [];
        this.$refs.seatPlanComponent.selectSeatsFromFilter(this.selectedSeats);

        await this.fetchSeats();

      } catch (error) {
        console.error(error);
        alert("Failed to book seats");
      }
    },
    async fetchSeats() {
      try {
        const response = await fetch("http://localhost:8080/api/flights/flight/" + this.flight.id);
        if (!response.ok) {
          throw new Error("Failed to fetch data");
        }

        const data = await response.json();
        this.$store.commit("setFlightData", { flight: data.flight, seatPlan: data.seats });

      } catch (error) {
        console.error(error);
      }
    },
  },
};
</script>

<style scoped>
.body {
  margin-top: 5px;
  background-image: url("../assets/gradient.png");
  border-radius: 20px;
  padding: 20px;
}

.flight {
  display: flex;
  justify-content: center;
  align-items: center;
}

.filters {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  padding: 10px;
  border-radius: 10px;
}

.filter {
  display: flex;
  align-items: center;
  gap: 5px;
  background-color: #fef2ea;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 4px 6px -2px rgba(0, 0, 0, 0.2);
  cursor: pointer;
}

select {
  padding: 5px;
  border-radius: 5px;
}

select, input[type="checkbox"] {
  padding: 5px;
  border-radius: 5px;
}

.seatData {
  display: flex;
  justify-content: center;
  align-items: center;
}
.seat-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
  max-width: 25vw;
}

.selected-seats h3, .total-price h4 {
  margin: 0;
}

.selected-seats {
  display: flex;
  flex-direction: column;
  gap: 5px;
  padding: 30px 20px 10px;
  background-color: #fef2ea;
  border-radius: 10px;
  box-shadow: 0 4px 6px -2px rgba(0, 0, 0, 0.2);
  cursor: pointer;
}

.selected-seats ul {
  list-style-type: disc;
  padding-left: 20px;
}

.selected-seats li {
  margin: 5px 0;
}

.total-price {
  padding: 10px 20px;
  background-color: #fef2ea;
  border-radius: 10px;
  box-shadow: 0 4px 6px -2px rgba(0, 0, 0, 0.2);
  cursor: pointer;
}

.seat-selector {
  display: flex;
  align-items: center;
  gap: 5px;
}

.seat-selector button {
  padding: 5px 10px;
  border: none;
  background-color: #ff7f50;
  color: white;
  cursor: pointer;
  border-radius: 5px;
}

.seat-selector button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.seat-plan {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
  padding: 10px;
  border-radius: 10px;
  min-width: 25vw;
}
.button-container {
  display: flex;
  justify-content: center;
}

button {
  padding: 5px 10px;
  margin-top: 10px;
  margin-bottom: 10px;
  border: none;
  background-color: #ff7f50;
  color: white;
  border-radius: 5px;
  cursor: pointer;
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

.src-button:hover, .back buton:hover, button:hover{
  background-color: #c7562b;
}


</style>