package com.tkppp.troll_catcher.summoner.domain

import com.tkppp.troll_catcher.summoner.util.MatchInfo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SummonerRepositoryTest(
    @Autowired private val summonerRepository: SummonerRepository
) {

    @AfterEach
    fun tearDown() {
        //summonerRepository.deleteAll()
    }

    @Test
    @DisplayName("Create 테스트")
    fun createTest() {
        // given
        val puuid = "puuid"
        val name = "쳇바퀴 속 다람쥐"
        val recentMatchId = "match_id"
        val matchInfos = listOf(
            MatchInfo(
                "KR_1234", 12, "asdf", "UTILITY",1, 2, 3, 40, 23, 33
            )
        )
        val summoner = Summoner(
            puuid = puuid, name = name, recentMatchId = recentMatchId, matchInfos = matchInfos
        )

        // when
        val result = summonerRepository.save(summoner)

        // then
        println(result.id)
        assertThat(result.puuid).isEqualTo(puuid)
        assertThat(result.name).isEqualTo(name)
        assertThat(result.recentMatchId).isEqualTo(recentMatchId)
        assertThat(result.matchInfos).isEqualTo(matchInfos)
    }
}