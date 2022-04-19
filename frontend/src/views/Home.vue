<template>
  <navbar />
  <div class="home-content-container">
    <div class="home-content-wrapper">
      <h1>LOL 트롤 캐쳐</h1>
      <div class="home-search-bar-wrapper">
        <input
          type="text"
          class="home-search-bar"
          v-model="summonerName"
          placeholder="소환사 이름 검색"
          spellcheck="false"
          autocapitalize="none"
          @keyup.enter="goToSearchResultPage"
        />
        <img class="home-search-icon" src="../assets/search-icon.png" @click="goToSearchResultPage">
      </div>
      <p>
        본 페이지는 게임에 열의가 없는, 맘대로 잘 풀리지 않으면 일방적으로
        달리고 던지는 소환사를 사전에 파악하기 위한 목적으로 제작되었습니다.
      </p>
    </div>
  </div>
</template>

<script>
import Navbar from '../components/Navbar.vue';
export default {
  components: {
    Navbar,
  },
  data(){
    return {
      summonerName: ''
    }
  },
  methods: {
    goToSearchResultPage() {
      this.$router.push(`/search/single?summoner=${this.summonerName}`)
    }
  }
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

.home-search-bar {
  box-sizing: border-box;
  margin: 30px 0px;
  width: 500px;
  height: 50px;
  padding: 0px 40px 0px 16px;

  border: none;
  font-size: 15px;
}

.home-search-bar:focus {
  outline: none;
}

.home-search-bar-wrapper {
  position: relative;
}

.home-search-icon {
  position: absolute;
  top: 43px;
  right: 8px;
}

.home-search-icon:hover {
  cursor: pointer;
}
</style>
