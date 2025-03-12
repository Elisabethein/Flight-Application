<template>
  <div class="flight" :class="{ 'non-clickable': disableClick }" @click="handleFlightClick">
    <img :src="getRandomImage()" alt="Travel destination" class="Picture" />

    <div class="metadata">
      <h1>Destination: {{ destination }}</h1>
      <p>From: {{ from }}</p>
      <p>Departure: {{ handleTimeFormat(departure) }}</p>
      <p>Arrival: {{ handleTimeFormat(arrival) }}</p>
      <p>Duration: {{ duration }}</p>
      <p>Price: {{ price }} € </p>
    </div>
  </div>
</template>

<script>
import boatsImage from "../assets/boats.png";
import riverImage from "../assets/river.png";
import streetImage from "../assets/street.png";
 export default {
   name: "FlightComponent",
   props: {
     id: String,
     destination: String,
     from: String,
     departure: String,
     arrival: String,
     duration: String,
     price: Number,
     disableClick: Boolean,
   },
   methods: {
     getRandomImage() {
       const images = [boatsImage, riverImage, streetImage];
       const randomIndex = Math.floor(Math.random() * images.length);
       return images[randomIndex];
     },
     async handleFlightClick() {
       if (this.disableClick) {
         return;
       }
       try {
         const response = await fetch("http://localhost:8080/api/flights/flight/" + this.id);
         if (!response.ok) {
           if (response.status === 404) {
              alert("Flight not found");
           } else {
             throw new Error("Failed to fetch data");
           }
         }

         const data = await response.json();
         const flight = data.flight;
         const seatPlan = data.seats;

         this.$store.commit("setFlightData", { flight, seatPlan });

          this.$router.push("/flight");
       } catch (error) {
         console.error(error);
       }
     },
     handleTimeFormat(time) {
       return new Date(time).toLocaleString();
     },
   }
 };
</script>

<style scoped>
.flight {
  display: inline-flex;
  justify-content: flex-start;
  align-items: stretch;
  background-color: #fef2ea;
  border-radius: 20px;
  margin: 20px;
  padding: 20px 40px 20px 20px;
  box-sizing: border-box;
  box-shadow: 0 4px 6px -2px rgba(0, 0, 0, 0.2);
  transition: box-shadow 0.2s ease-in-out;
  width: 45vw;
}

.flight:not(.non-clickable):hover {
  cursor: pointer;
  box-shadow: 0 6px 10px -2px rgba(0, 0, 0, 0.3);
}

.non-clickable {
  pointer-events: none;
}

.Picture {
  height: 100%;
  width: auto;
  max-width: 300px;
  object-fit: cover;
  border-radius: 10px;
}

.metadata {
  text-align: left;
  margin-left: 20px;
  flex-grow: 1;
}
</style>