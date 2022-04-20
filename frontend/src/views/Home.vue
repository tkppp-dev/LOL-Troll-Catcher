<template>
  <navbar />
  <div class="home-content-container">
    <div class="home-content-wrapper">
      <h1>LOL 트롤 캐쳐</h1>
      <searchbar :isHome="true" />
      <p>
        본 페이지는 게임에 열의가 없는, 맘대로 잘 풀리지 않으면 일방적으로
        달리고 던지는 소환사를 사전에 파악하기 위한 목적으로 제작되었습니다.
      </p>
    </div>
  </div>
</template>

<script>
import Navbar from '../components/Navbar.vue';
import Searchbar from '../components/Searchbar.vue';
export default {
  components: {
    Navbar,
    Searchbar,
  },
};
</script>

<style>
.home-content-container {
  display: flex;
  justify-content: center;
  width: 100%;
}
.home-content-wrapper {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 200px 0px 0px 0px;
  width: 960px;
}
.home-content-wrapper > h1 {
  margin-bottom: 10px;
}

@media (max-width: 960px){
  .home-content-wrapper{
    width: 98%
  }
}
</style>
