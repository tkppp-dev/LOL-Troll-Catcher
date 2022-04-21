<template>
  <div class="nav-container">
    <div class="nav-content">
      <span class="nav-title" @click="goToHome">
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
  methods: {
    goToHome(){
      this.$router.push('/')
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
  width: 960px;
  padding: 12px 0px;

  display: flex;
  justify-content: flex-start;
  align-items: center;

  color: white;
  font-weight: bold;
  font-size: 21px;
}

.nav-title {
  cursor: pointer;
}

.nav-search-bar-wrapper {
  display: flex;
  flex: 1;
  justify-content: flex-end;
  align-items: center;
}

@media (max-width: 960px) {
  .nav-content {
    width: 96%
  }
}

@media (max-width: 383px){
  .nav-content {
    flex-direction: column;
  }
  .nav-search-bar-wrapper{
    justify-content: center;
    width: 100%;
  }
}
</style>