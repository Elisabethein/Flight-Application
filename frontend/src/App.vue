<template>
  <div id="app">
    <HeaderComponent />
    <div class="pt-32"></div>

    <main class="content">
      <router-view></router-view>
    </main>
  </div>
</template>

<script>
import HeaderComponent from './components/HeaderComponent.vue';
export default {
  name: "App",
  components: {
    HeaderComponent,
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Junge&display=swap');
#app {
  font-family: 'Junge',cursive;
}
</style>
