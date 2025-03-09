import { createStore } from 'vuex';

const store = createStore({
    state() {
        return {
            flight: null,
            seatPlan: [],
        };
    },
    mutations: {
        setFlightData(state, { flight, seatPlan }) {
            state.flight = flight;
            state.seatPlan = seatPlan;
        },
    },
});
export default store;