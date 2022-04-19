<template>
  <div class="nav-container">
    <div class="nav-content">
      <span class="nav-title">
        LOL 트롤 캐쳐
      </span>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style>
.nav-container {
  display: flex;
  justify-content: center;
  width: 100%;
  background-color: rgb(45,47,56);
  
  box-shadow: 0 3px 3px rgba(45,47,56, 0.5);
  z-index: 999;
}

.nav-content {
  height: 56px;
  width: 960px;

  display: flex;
  justify-content: flex-start;
  align-items: center;

  color: white;
  font-weight: bold;
  font-size: 21px;
}
</style>