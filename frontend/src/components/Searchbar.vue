<template>
  <div
    class="search-bar-wrapper"
    :class="{ 'home-search-bar-wrapper': isHome, 'nav-search-bar-wrapper': isNavbar }"
  >
    <input
      type="text"
      class="search-bar"
      :class="{ 'home-search-bar': isHome, 'nav-search-bar': isNavbar }"
      v-model="summonerName"
      placeholder="소환사 이름 검색"
      spellcheck="false"
      autocapitalize="none"
      @keyup.enter="goToSearchResultPage"
    />
    <img
      class="search-icon"
      :class="{ 'home-search-icon': isHome, 'nav-search-icon': isNavbar }"
      src="../assets/search-icon.png"
      @click="goToSearchResultPage"
    />
  </div>
</template>

<script>
export default {
  name: 'Searchbar',
  props: {
    isHome: {
      type: Boolean,
      default: false,
    },
    isNavbar: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      summonerName: '',
    };
  },
  methods: {
    goToSearchResultPage() {
      if(this.summonerName.length < 2){
        alert('소환사 이름은 두글자 이상이어야 합니다')
      } else {
        this.$router.push(`/search/single?summoner=${this.summonerName}`);
      }
    },
  },
};
</script>

<style>
.search-bar {
  box-sizing: border-box;
  border: none;
}

.home-search-bar {
  margin: 30px 0px;
  width: 500px;
  height: 50px;
  padding: 0px 40px 0px 16px;

  font-size: 15px;
}

.nav-search-bar {
  width: 200px;
  height: 30px;
  padding: 0px 30px 0px 15px;

  font-size: 14px;
}

@media (max-width: 540px) {
  .home-search-bar-wrapper {
    display: flex;
    width: 80%;
  }
}

@media (max-width: 383px) {
  .nav-search-bar-wrapper {
    display: flex;
    width: 80%;
  }
  .nav-search-bar {
    width: auto;
    flex: 1;
    margin-top: 12px;
  }
  .nav-search-icon {
    margin-top: 12px;
  }
}

.search-bar:focus {
  outline: none;
}

.search-bar-wrapper {
  position: relative;
}

.search-icon {
  position: absolute;
}

.home-search-icon {
  top: 43px;
  right: 8px;
}

.nav-search-icon {
  width: 20px;
  right: 5px;
  top: 5px;
}

.search-icon:hover {
  cursor: pointer;
}
</style>
