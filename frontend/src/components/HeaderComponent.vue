<template>
  <body>
  <header class="header">
    <div class="logo-container">
      <img src="../assets/plane_exp.png" alt="Logo" class="logo" />
    </div>
  </header>
  </body>
</template>

<script >
export default {
  name: "HeaderComponent",
};
</script>

<style scoped>
body {
  margin: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}
.header {
  width: 100vw;
  height: auto;
  display: flex;
  justify-content: center;
  align-items: center;
}

.logo-container {
  width: 100%;
  height: auto;
  display: flex;
  justify-content: center;
  align-items: center;
}

.logo {
  width: auto;
  height: 100%;
  max-width: 100%;
  object-fit: contain;
  border-radius: 20px;
}

</style>