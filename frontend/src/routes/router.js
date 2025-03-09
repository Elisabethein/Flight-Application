import { createRouter, createWebHistory } from 'vue-router';
import MainPage from "@/pages/MainPage.vue";
import FlightPage from "@/pages/FlightPage.vue";

const routes = [
    {
        path: '/',
        name: 'MainPage',
        component: MainPage,
    },
    {
        path: '/flight',
        name: 'Flight',
        component: FlightPage,
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;