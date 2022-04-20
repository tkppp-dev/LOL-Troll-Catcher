<template>
  <div class="nav-container">
    <div class="nav-content">
      <span class="nav-title">
        LOL 트롤 캐쳐
      </span>
      <div class="nav-search-bar-wrapper">
        <searchbar v-if="navSearchbarVisible" :isNavbar="true"/>
      </div>
    </div>
  </div>
</template>

<script>
import Searchbar from './Searchbar.vue'
export default {
  components: { Searchbar },
  data(){
    return {
      navSearchbarVisible: true
    }
  },
  created(){
    if(this.$route.path === '/'){
      this.navSearchbarVisible = false
    }
  }
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

.nav-search-bar-wrapper {
  display: flex;
  flex: 1;
  justify-content: flex-end;
  align-items: center;
}

.nav-search-bar {
  box-sizing: border-box;
  width: 250px;
  height: 30px;
  padding: 12px;
}

@media (max-width: 960px) {
  .nav-content {
    width: 98%
  }
}
</style>