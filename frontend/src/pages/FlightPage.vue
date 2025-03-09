<template>
  <div class="body">
  <div class="flight">
    <FlightComponent
        :destination="flight?.destination"
        :from="flight?.departure"
        :departure="flight?.departureTime"
        :arrival="flight?.arrivalTime"
        :duration="flight?.flightTime"
        :price="flight?.price"
        :disableClick="true"
    />
  </div>

    <div class="filters">
      <div class="filter">
        <label for="class">Class:</label>
        <input type="text" id="class" name="class" />
      </div>
      <div class="filter">
        <label for="withLegRoom">Extra leg room:</label>
        <input type="text" id="withLegRoom" name="withLegRoom" />
      </div>
      <div class="filter">
        <label for="nearExit">Near exit:</label>
        <input type="text" id="nearExit" name="nearExit" />
      </div>
      <div class="filter">
        <label for="number">How many seats:</label>
        <input type="number" id="number" name="number" />
      </div>
      <div class="filter">
        <button @click="showSeats">Search</button>
      </div>
    </div>

  </div>
</template>

<script>
import { mapState } from "vuex";
import FlightComponent from "@/components/FlightComponent.vue";

export default {
  name: "FlightPage",
  components: {
    FlightComponent
  },
  computed: {
    ...mapState(["flight", "seatPlan"]),
  },
};
</script>

<style scoped>
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
}
</style>