<template>
  <div class="connecting-flight">
    <FlightComponent
        :destination="firstFlight.destination"
        :from="firstFlight.departure"
        :departure="firstFlight.departureTime"
        :arrival="firstFlight.arrivalTime"
        :duration="firstFlight.flightTime"
        :price="firstFlight.price"
        :id="firstFlight.id"
    />

    <div class="connection-info">
      <p class="arrow">→</p>
      <p class="layover-time">Layover: {{ layoverTime }}</p>
      <p class="connecting-text">Connecting Flight</p>
    </div>

    <FlightComponent
        :destination="secondFlight.destination"
        :from="secondFlight.departure"
        :departure="secondFlight.departureTime"
        :arrival="secondFlight.arrivalTime"
        :duration="secondFlight.flightTime"
        :price="secondFlight.price"
        :id="secondFlight.id"
    />
  </div>
</template>

<script>
import FlightComponent from "@/components/FlightComponent.vue";

export default {
  name: "ConnectingFlightComponent",
  components: { FlightComponent },
  props: {
    firstFlight: {
      type: Object,
      required: true
    },
    secondFlight: {
      type: Object,
      required: true
    }
  },
  computed: {
    layoverTime() {
      const arrival = new Date(this.firstFlight.arrivalTime);
      const departure = new Date(this.secondFlight.departureTime);
      const diffMs = departure - arrival;
      const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
      const diffMinutes = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60));
      return `${diffHours}h ${diffMinutes}m`;
    }
  }
};
</script>

<style scoped>
.connecting-flight {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 15px;
  margin-top: 10px;
  background: url("../assets/gradient2.png");
  background-size: cover;
  border-radius: 20px;
  width: 90vw;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.connection-info {
  text-align: center;
  font-weight: bold;
}

.arrow {
  font-size: 24px;
  margin: 5px 0;
}

.layover-time {
  font-size: 14px;
}

.connecting-text {
  font-size: 12px;
}
</style>
