<template>
  <div class="main">
    <div class="filters">
      <div class="filter">
        <label for="from">From:</label>
        <input type="text" id="from" name="from" />
      </div>
      <div class="filter">
        <label for="to">To:</label>
        <input type="text" id="to" name="to" />
      </div>
      <div class="filter">
        <label for="date">Date:</label>
        <input type="date" id="date" name="date" />
      </div>
      <div class="filter">
        <label for="price">Price:</label>
        <input type="number" id="price" name="price" />
      </div>
      <div class="filter">
        <button>Search</button>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  name: "MainPage",
};
</script>

<style scoped>
.main {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 30px 30px 100px;
  box-sizing: border-box;
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

input {
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

button {
  padding: 5px 10px;
  border: none;
  background-color: #ff7f50;
  color: white;
  border-radius: 5px;
  cursor: pointer;
}
</style>