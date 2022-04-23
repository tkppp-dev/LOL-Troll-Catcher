<template>
  <div class="match-info-wrapper">
    <div class="match-info-col match-info-meta-wrapper">
      <div class="match-info-game-result">{{matchResult}}</div>
      <div class="match-info-duration">{{minute}}분 {{second}}초</div>
    </div>
    <div class="match-info-col match-info-champion-img-wrapper">
      <img class="match-info-champion-img" :src="championImg" />
    </div>
    <div class="match-info-col">
      <img class="match-info-spell" :src="spell1Img" />
      <img class="match-info-spell" :src="spell2Img" />
    </div>
    <div class="match-info-col match-info-kda-wrapper">
      <div class="match-info-kda">
        {{ matchInfo.kills }} / {{ matchInfo.deaths }} / {{ matchInfo.assists }}
      </div>
      <div class="match-info-kda-ratio">{{kdaRatio}}</div>
    </div>
    <div class="match-info-col match-info-analysis">
      <div class="match-info-col-name">트롤 가능성</div>
      <div class="match-info-possibility">{{ trollPossibility }}</div>
    </div>
  </div>
</template>

<script>
import SummonerSpell from '../util/summoner-spell';

export default {
  name: 'MatchInfo',
  props: {
    matchInfo: Object,
  },
  data() {
    return {
      matchResult: '',
      minute: '',
      second: '',
      championImg: '',
      spell1Img: '',
      spell2Img: '',
      trollPossibility: '',
      kdaRatio: ''
    };
  },
  methods: {
    getSummonerSpellImgUrl(id, summonerSpell) {
      for (let spellData of summonerSpell.data) {
        if (spellData.key == id) {
          return `http://ddragon.leagueoflegends.com/cdn/12.7.1/img/spell/${spellData.id}.png`;
        }
      }
    },
    getTrollPossibility(possibility) {
      if (possibility === 'VERY_HIGH') return '매우 높음';
      else if (possibility === 'HIGH') return '높음';
      else if (possibility === 'OPACITY') return '애매함';
      else return '매우 낮음';
    },
  },
  created() {
    this.matchResult = this.matchInfo.matchResult ? '승리' : '패배'
    this.minute = Math.floor(this.matchInfo.duration / 60)
    this.second = this.matchInfo.duration % 60
    this.championImg = `http://ddragon.leagueoflegends.com/cdn/12.7.1/img/champion/${this.matchInfo.champion}.png`;
    this.spell1Img = this.getSummonerSpellImgUrl(this.matchInfo.summonerSpell1Id, SummonerSpell);
    this.spell2Img = this.getSummonerSpellImgUrl(this.matchInfo.summonerSpell2Id, SummonerSpell);
    this.trollPossibility = this.getTrollPossibility(
      this.matchInfo.trollPossibility
    );
    if(this.matchInfo.deaths === 0 ) this.kdaRatio = 'Perfect'
    else this.kdaRatio = ((this.matchInfo.kills + this.matchInfo.assists)/this.matchInfo.deaths).toFixed(2)
  },
};
</script>

<style>
.match-info-wrapper {
  display: flex;
  width: 100%;
  align-items: center;
  margin-bottom: 9px;
}

.match-info-col {
  display: flex;
  flex-direction: column;
}

.match-info-meta-wrapper {
  width: 60px;
  margin-right: 9px;
}
.match-info-game-result {
  font-size: 14px;
  font-weight: bold;
}

.match-info-duration {
  font-size: 13px;
}

.match-info-champion-img-wrapper {
  margin-right: 9px;
}

.match-info-champion-img {
  width: 50px;
  border-radius: 25px;
}

.match-info-spell {
  width: 24px;
  border-radius: 4px;
}

.match-info-spell:first-child {
  margin-bottom: 2px;
}

.match-info-kda-wrapper {
  box-sizing: border-box;
  width: 120px;
  padding: 0px 18px;
}

.match-info-kda {
  font-size: 15px;
  font-weight: bold;
}

.match-info-kda-ratio {
  font-size: 13px;
  color: gray;
}
.match-info-analysis {
  box-sizing: border-box;
  width: 100px;
}

.match-info-col-name {
  font-weight: bold;
  font-size: 15px;
}
.match-info-possibility {
  font-size: 14px;
}
</style>
