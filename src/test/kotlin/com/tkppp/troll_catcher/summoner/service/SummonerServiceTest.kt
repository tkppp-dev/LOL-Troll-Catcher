package com.tkppp.troll_catcher.summoner.service

import com.tkppp.troll_catcher.exception.CustomException
import com.tkppp.troll_catcher.exception.ErrorCode
import com.tkppp.troll_catcher.summoner.domain.Summoner
import com.tkppp.troll_catcher.summoner.domain.SummonerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class SummonerServiceTest(
    @Autowired private val summonerService: SummonerService,
    @Autowired private val summonerRepository: SummonerRepository
) {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("소환사 정보 받아오기 서비스 통합 테스트")
    inner class SummonerInfoTest {

        private lateinit var newSummoner: Summoner

        @AfterAll
        fun tearDown() {
            summonerRepository.delete(newSummoner)
        }

        @Test
        @DisplayName("DB에 존재하지 않지만 존재하는 소환사 이름을 받을 경우 DB에 소환사 정보를 저장하고 puuid 를 반환한다")
        fun getSummonerInfo_ExistSummonerNotInDB_shouldReturnPuuid() {
            // given
            val summonerName = "쳇바퀴 속 다람쥐"

            // when
            val result = summonerService.getSummonerInfo(summonerName)

            // then
            newSummoner = summonerRepository.findByName(summonerName)!!
            assertThat(result.puuid).isEqualTo(newSummoner.puuid)
            assertThat(summonerName).isEqualTo(newSummoner.name)
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
            assertThat(result.puuid).isEqualTo(summoner!!.puuid)
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

    @Nested
    @DisplayName("매치 ID 리스트 받아오기 서비스 통합 테스트")
    inner class SummonerMatchIdListTest {

        @Test
        @DisplayName("puuid 를 받아 해당 소환사의 매치 ID 리스트를 받아온다")
        fun getMatchDataList_shouldReturnMatchList(){
            // given
            val puuid = "ilAGh19BQqkCL2EhgK6609131BNNuF-fSV4lD441hGtpkoVv8DyGwK3qsrzjiCSw6O1Jd8alKPGdaA"

            // when
            val result = summonerService.getMatchIdList(puuid)

            // then
            assertThat(result).isInstanceOf(List::class.java)
            assertThat(result.size).isLessThanOrEqualTo(30)

            println(result)
        }

        @Test
        @DisplayName("유효하지 않는 puuid 를 받으면 CustomException 을 던져야한다")
        fun getMatchDataList_shouldThrowException(){
            // given
            val puuid = "sdfasdfsfasf"

            // when
            val result = assertThrows<CustomException> { summonerService.getMatchIdList(puuid) }

            // then
            assertThat(result).isInstanceOf(CustomException::class.java)
            assertThat(result.error).isEqualTo(ErrorCode.GET_MATCH_ID_LIST_FAIL)
        }
    }

    @Nested
    @DisplayName("매치 데이터 받아오기 서비스 통합 테스트")
    inner class SummonerMatchDataTest {

        private val puuid = "ilAGh19BQqkCL2EhgK6609131BNNuF-fSV4lD441hGtpkoVv8DyGwK3qsrzjiCSw6O1Jd8alKPGdaA"


        @Test
        @DisplayName("매치 Id 리스트와 가장 최근 조회했던 매치 Id를 받아 매치 데이터를 받아온다")
        fun getMatchDataList_shouldReturnMatchDataList() {
            // given
            val matchList = "KR_5864016614, KR_5863771894, KR_5863770401, KR_5862042714, KR_5861937125, KR_5861941335, KR_5861904421, KR_5861769917, KR_5860884087, KR_5860819198, KR_5860744843, KR_5855890726, KR_5855835385, KR_5855116233, KR_5855094651, KR_5855092139, KR_5855078411, KR_5855005233, KR_5854615038, KR_5854670236"
                .split(',').map { it.trim() }
            val recentMatchId = "KR_5855094651"

            // when
            val result = summonerService.getMatchInfoList(matchList, recentMatchId, puuid)

            // given
            assertThat(result).isInstanceOf(List::class.java)
            assertThat(result[0]).isInstanceOf(HashMap::class.java)
            assertThat(result.size).isEqualTo(14)
        }

        @Test
        @DisplayName("잘못된 매치 Id 리스트를 받을 경우 CustomException 을 던진다")
        fun getMatchDataList_shouldThrowException() {
            // given
            val matchList = listOf("invalid1", "invalid2")
            val recentMatchId = "invalid0"

            // when
            val result = assertThrows<CustomException> {  summonerService.getMatchInfoList(matchList, recentMatchId, puuid) }

            // given
            assertThat(result.error).isEqualTo(ErrorCode.GET_MATCH_DATA_FAIL)
        }
    }

    @Test
    fun getResult(){
        //summonerService.getSingleSearchResult("쳇바퀴 속 다람쥐")
    }
}