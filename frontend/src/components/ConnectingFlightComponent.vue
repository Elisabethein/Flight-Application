<template>
  <div class="connecting-flight">
    <div class="flight-details">
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
    <div class="connection-info">
      <p>Connecting Flight</p>
    </div>
  </div>
</template>

<script>
import FlightComponent from "@/components/FlightComponent.vue";

export default {
  name: "ConnectingFlightComponent",
  components: { FlightComponent },
  props: {
    flights: {
      type: Array,
      required: true
    }
  }
};
</script>

<style scoped>
.connecting-flight {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 20px;
}

.flight-details {
  display: flex;
  gap: 20px;
}

.connection-info {
  font-size: 14px;
  color: #666;
  margin-top: 10px;
}
</style>
