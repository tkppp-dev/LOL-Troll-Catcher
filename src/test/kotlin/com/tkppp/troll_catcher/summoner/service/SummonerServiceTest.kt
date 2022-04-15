package com.tkppp.troll_catcher.summoner.service

import com.tkppp.troll_catcher.exception.CustomException
import com.tkppp.troll_catcher.exception.ErrorCode
import com.tkppp.troll_catcher.summoner.domain.SummonerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class SummonerServiceTest(
    @Autowired private val summonerService: SummonerService,
    @Autowired private val summonerRepository: SummonerRepository
) {

    @Nested
    @DisplayName("소환사 정보 서비스 통합 테스트")
    inner class SummonerInfo(){

        @Test
        @DisplayName("DB에 존재하지 않지만 존재하는 소환사 이름을 받을 경우 DB에 소환사 정보를 저장하고 puuid 를 반환한다")
        fun getSummonerInfo_ExistSummonerNotInDB_shouldReturnPuuid() {
            // given
            val summonerName = "쳇바퀴 속 다람쥐"

            // when
            val result = summonerService.getSummonerInfo(summonerName)

            // then
            val summoner = summonerRepository.findByName(summonerName)
            assertThat(result).isEqualTo(summoner!!.puuid)
            assertThat(summonerName).isEqualTo(summoner.name)
        }

        @Test
        @DisplayName("DB에 존재하는 소환사 이름을 받을 경우 puuid 를 반환한다")
        fun getSummonerInfo_ExistSummonerInDB_shouldReturnPuuid() {
            // given
            val summonerName = "쳇바퀴 속 다람쥐"

            // when
            val result = summonerService.getSummonerInfo(summonerName)

            // then
            val summoner = summonerRepository.findByName(summonerName)
            assertThat(result).isEqualTo(summoner!!.puuid)
            assertThat(summonerName).isEqualTo(summoner.name)
        }


        @Test
        @DisplayName("존재하지 않는 소환사 이름을 받을 경우 CustomException 을 던져야한다")
        fun getSummonerInfo_NotExistSummoner_shouldThrowException() {
            // given
            val summonerName = "쳇바퀴알 속 다나부"

            // when
            val exception = assertThrows<CustomException> {
                summonerService.getSummonerInfo(summonerName)
            }

            // then
            assertThat(exception.error).isEqualTo(ErrorCode.SUMMONER_DOESNT_EXIST)
        }
    }
}