<template>
  <navbar />
  <div v-if="isReady">
    <div class="single-search-content-container">
      <div v-if="isSummonerExist">
        <div class="single-search-analysis-wrapper">
          <div class="single-search-summoner-name-wrapper">
            <h1>{{ summonerName }}</h1>
            <h2>분석 결과</h2>
          </div>
          <div class="single-search-result-wrapper">
            <div class="single-search-result-title">
              최근 10게임 트롤 의심 게임 비율 :
            </div>
            <div
              class="single-search-result"
              :class="{
                'single-search-result-high':
                  analysisResult.last10games.step.high,
                'single-search-result-opacity':
                  analysisResult.last10games.step.opacity,
                'single-search-result-no-possibility':
                  analysisResult.last10games.step.noPossibility,
              }"
            >
              {{ analysisResult.last10games.value }}%
            </div>
          </div>
          <div class="single-search-result-wrapper">
            <div class="single-search-result-title">
              최근 30게임 트롤 의심 게임 비율 :
            </div>
            <div
              class="single-search-result"
              :class="{
                'single-search-result-high':
                  analysisResult.last30games.step.high,
                'single-search-result-opacity':
                  analysisResult.last30games.step.opacity,
                'single-search-result-no-possibility':
                  analysisResult.last30games.step.noPossibility,
              }"
            >
              {{ analysisResult.last30games.value }}%
            </div>
          </div>
          <div class="single-search-result-wrapper">
            <div class="single-search-result-title">트롤 가능성 :</div>
            <div
              class="single-search-result"
              :class="{
                'single-search-result-high':
                  analysisResult.trollPossibilty.step.high,
                'single-search-result-opacity':
                  analysisResult.trollPossibilty.step.opacity,
                'single-search-result-no-possibility':
                  analysisResult.trollPossibilty.step.noPossibility,
              }"
            >
              {{ analysisResult.trollPossibilty.value }}
            </div>
          </div>
        </div>
        <div class="single-search-match-wrapper">
          <h2>매치 정보</h2>
          <div class="match-info-container">
            <match-info
              v-for="(matchInfo, idx) in matchInfos"
              :key="idx"
              :matchInfo="matchInfo"
            />
          </div>
        </div>
      </div>
      <div v-if="!isSummonerExist" class="single-search-summoner-not-exist">
        존재하지 않는 소환사이거나 정보를 불러오는 도중 문제가 발생했습니다.
      </div>
    </div>
  </div>
  <div v-if="!isReady" class="single-search-loading">
    {{ loadingComment }}
  </div>
</template>

<script>
import MatchInfo from '../components/MatchInfo.vue';
import Navbar from '../components/Navbar.vue';
import axios from 'axios';

export default {
  components: { Navbar, MatchInfo },
  data() {
    return {
      isReady: false,
      isSummonerExist: true,
      loadingComment: '',
      summonerName: '',
      analysisResult: {
        last10games: {
          value: 0,
          step: {
            high: false,
            opacity: false,
            noPossibility: false,
          },
        },
        last30games: {
          value: 0,
          step: {
            high: false,
            opacity: false,
            noPossibility: false,
          },
        },
        trollPossibilty: {
          value: '',
          step: {
            high: false,
            opacity: false,
            noPossibility: false,
          },
        },
      },
      matchInfos: [],
    };
  },
  methods: {
    getPossibility(possibility) {
      switch (possibility) {
        case ('VERY_HIGH', 'HIGH'):
          return true;
        default:
          return false;
      }
    },
  },
  async created() {
    try {
      let intervalCnt = 0;
      const loading = setInterval(() => {
        if (intervalCnt % 3 == 0) {
          this.loadingComment = '분석중 .';
        } else if (intervalCnt % 3 == 1) {
          this.loadingComment = '분석중 . .';
        } else {
          this.loadingComment = '분석중 . . .';
        }
        intervalCnt++;
      }, 500);
      const summonerName = this.$route.query.summoner;
      this.summonerName = summonerName;

      const res = await axios.get(
        `/api/search/single?summonerName=${summonerName}`
      );

      this.matchInfos = res.data;

      let recent10count = 0;
      let recent30count = 0;

      this.matchInfos.forEach((el, idx) => {
        const isTroll = this.getPossibility(el.trollPossibility);
        if (isTroll) {
          if (idx < 10) {
            recent10count++;
          }
          recent30count++;
        }
      });

      const len10 = this.matchInfos.length < 10 ? this.matchInfos.length : 10;
      this.analysisResult.last10games.value = Math.round(
        (recent10count / len10) * 100
      );

      this.analysisResult.last30games.value = Math.round(
        (recent30count / this.matchInfos.length) * 100
      );

      if (this.analysisResult.last10games.value >= 40) {
        this.analysisResult.trollPossibilty.value = '매우 높음';
        this.analysisResult.trollPossibilty.step.high = true;
      } else if (this.analysisResult.last10games.value >= 30) {
        this.analysisResult.trollPossibilty.value = '높음';
        this.analysisResult.trollPossibilty.step.high = true;
      } else if (this.analysisResult.last10games.value >= 20) {
        this.analysisResult.trollPossibilty.value = '낮음';
        this.analysisResult.trollPossibilty.step.opacity = true;
      } else {
        this.analysisResult.trollPossibilty.value = '매우 낮음';
        this.analysisResult.trollPossibilty.step.noPossibility = true;
      }

      this.isReady = true;
      clearImmediate(loading);
    } catch (e) {
      this.isReady = true
      this.isSummonerExist = false
    }
  },
};
</script>

<style>
.single-search-content-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 30px;
}

.single-search-summoner-not-exist{
  margin-top: 100px;
  font-size: 18px;
  font-weight: bold;
}

.single-search-loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;

  font-size: 21px;
  font-weight: bold;
}

.single-search-analysis-wrapper {
  width: 960px;
}

.single-search-summoner-name-wrapper {
  display: flex;
  align-items: flex-end;
  margin-bottom: 24px;
}

.single-search-summoner-name-wrapper > h1,
h2 {
  margin: 0px;
}

.single-search-summoner-name-wrapper > h1 {
  margin-right: 9px;
}

.single-search-result-wrapper {
  display: flex;
  margin-top: 12px;
  font-size: 18px;
  font-weight: bold;
}

.single-search-result {
  margin-left: 5px;
}

.single-search-result-high {
  color: red;
}

.single-search-result-opacity {
  color: darkorange;
}

.single-search-result-no-possibility {
  color: green;
}

.single-search-match-wrapper {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 960px;
  margin-top: 36px;
}

.match-info-container {
  margin-top: 18px;
}

@media (max-width: 960px) {
  .single-search-analysis-wrapper {
    width: 96%;
  }

  .single-search-match-wrapper {
    width: 96%;
  }
}
</style>
