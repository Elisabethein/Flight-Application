<template>
  <div class="body">


  <div class="seat-plan">
    <!-- First Exit (Before Row 1) -->
    <div class="exit-row">← EXIT →</div>

    <div v-for="(row, rowIndex) in seatRows" :key="rowIndex">
      <!-- Middle Exit (Before Row 6) -->
      <div v-if="rowIndex === 5" class="exit-row">← EXIT →</div>

      <div class="row">
        <!-- Left Side Seats -->
        <div class="seat-container">
          <div
              v-for="(seat, seatIndex) in row.left"
              :key="seat.seatNumber"
              class="seat"
              :class="{
                [seat.seatClass]: !seat.booked && !selectedSeats.includes(seat),
                'booked-seat': seat.booked,
                'selected-seat': selectedSeats.includes(seat)
              }"
              @click="selectSeat(seat)"
          >
            <span v-if="seat.booked" class="booked">X</span>
            <span v-else>{{ seat.seatNumber }}</span>
          </div>
        </div>

        <!-- Row Number in Aisle -->
        <div class="aisle">
          <span>{{ rowIndex + 1 }}</span>
        </div>

        <!-- Right Side Seats -->
        <div class="seat-container">
          <div
              v-for="(seat, seatIndex) in row.right"
              :key="seat.seatNumber"
              class="seat"
              :class="{
                [seat.seatClass]: !seat.booked && !selectedSeats.includes(seat),
                'booked-seat': seat.booked,
                'selected-seat': selectedSeats.includes(seat)
              }"
              @click="selectSeat(seat)"
          >
            <span v-if="seat.booked" class="booked">X</span>
            <span v-else>{{ seat.seatNumber }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Last Exit (After Row 10) -->
    <div class="exit-row">← EXIT →</div>
  </div>

  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "SeatPlanComponent",
  props: {
    seats: Array,
  },
  data() {
    return {
      selectedSeats: [],
    };
  },
  computed: {
    ...mapState(["flight", "seatPlan"]),
    seatRows() {
      if (!this.seatPlan || this.seatPlan.length !== 60) {
        console.error("There should be exactly 60 seats.");
        return [];
      }

      // Make a shallow copy of seatPlan and sort it by seatNumber
      const sortedSeats = [...this.seatPlan].sort((a, b) => {
        const [numA, letterA] = a.seatNumber.match(/(\d+)([A-F])/)?.slice(1) || [];
        const [numB, letterB] = b.seatNumber.match(/(\d+)([A-F])/)?.slice(1) || [];
        return numA - numB || letterA.localeCompare(letterB);
      });

      let rows = [];
      let totalSeats = sortedSeats.length;

      // Iterate over seatPlan in chunks of 6 for each row (3 seats left, 3 seats right)
      for (let i = 0; i < totalSeats; i += 6) {
        let leftSide = sortedSeats.slice(i, i + 3);  // First 3 seats in the row
        let rightSide = sortedSeats.slice(i + 3, i + 6);  // Next 3 seats in the row

        rows.push({
          left: leftSide,
          right: rightSide
        });
      }

      return rows;
    }


  },
  methods: {
    selectSeat(seat) {
      if (seat.booked) return; // Ignore booked seats

      const index = this.selectedSeats.findIndex(s => s.seatNumber === seat.seatNumber);

      if (index !== -1) {
        // Deselect seat if already selected
        this.selectedSeats.splice(index, 1);
      } else {
        // Add new seat object
        this.selectedSeats.push(seat);
      }

      // Emit full seat objects to parent
      this.$emit("update-selected-seats", this.selectedSeats);
    },
    selectSeatsFromFilter(seats) {
      this.selectedSeats = seats;
      this.$emit("update-selected-seats", this.selectedSeats);
    },
    selectDefaultSeat() {
      // Select first available Economy seat by default
      if (this.seatPlan && this.seatPlan.length > 0) {
        const defaultSeat = this.seatPlan.find(seat => seat.seatClass === "Economy" && !seat.booked);
        if (defaultSeat) {
          this.selectedSeats = [defaultSeat];
        }
      }

      this.$emit("update-selected-seats", this.selectedSeats);
    },
  },
  mounted() {
    this.selectDefaultSeat();
  }
};
</script>

<style scoped>
.seat-plan {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
  padding: 20px 40px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 10px -2px rgba(0, 0, 0, 0.2);
}

.row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.seat-container {
  display: flex;
  gap: 5px;
}

.seat {
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 5px;
  font-size: 14px;
  font-weight: bold;
  position: relative;
}

/* Class Colors */
.First {
  background-color: #888585; /* Darker shade for First Class */
}

.Business {
  background-color: #B2B2B2; /* Slightly lighter shade for Business */
}

.Economy {
  background-color: #D9D9D9; /* Lightest shade for Economy */
}

.booked-seat {
  background-color: #ff4c4c !important;
  color: white;
}

.booked {
  position: absolute;
  font-size: 18px;
  color: white;
}

.aisle {
  width: 40px;
  text-align: center;
  font-weight: bold;
  font-size: 16px;
}

.selected-seat {
  background-color: orange;
}

.exit-row {
  width: 100%;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  margin: 5px 0;
}
</style>
