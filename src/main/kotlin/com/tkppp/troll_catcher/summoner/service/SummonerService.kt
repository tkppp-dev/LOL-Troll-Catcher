package com.tkppp.troll_catcher.summoner.service

import com.tkppp.troll_catcher.exception.CustomException
import com.tkppp.troll_catcher.exception.ErrorCode
import com.tkppp.troll_catcher.summoner.domain.Summoner
import com.tkppp.troll_catcher.summoner.domain.SummonerRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.lang.Exception

@Service
class SummonerService(
    private val summonerRepository: SummonerRepository,
    private val restTemplate: RestTemplate,
    private val httpEntity: HttpEntity<String>
) {

    private val krBaseUri = "https://kr.api.riotgames.com/lol"
    private val asiaBaseUri = "https://asia.api.riotgames.com/lol"

    fun getSummonerInfo(summonerName: String): Summoner {
        val summoner = summonerRepository.findByName(summonerName)
        return when (summoner?.puuid) {
            null -> {
                try {
                    // restTemplate 은 uri 를 자동으로 인코딩하기 때문에 따로 할 필요 없다
                    val uri = "$krBaseUri/summoner/v4/summoners/by-name/$summonerName"
                    val body = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, HashMap::class.java).body!!
                    val entity = Summoner(puuid = body["puuid"] as String, name = body["name"] as String)
                    summonerRepository.save(entity)
                } catch (ex: Exception) {
                    throw CustomException(ErrorCode.SUMMONER_DOESNT_EXIST)
                }
            }
            else -> summoner
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getMatchIdList(puuid: String): List<String> =
        try {
            val param = "type=ranked&start=0&count=20"
            val uri = "$asiaBaseUri/match/v5/matches/by-puuid/${puuid}/ids?$param"
            restTemplate.exchange(uri, HttpMethod.GET, httpEntity, List::class.java).body!! as List<String>
        } catch (ex: Exception) {
            throw CustomException(ErrorCode.GET_MATCH_ID_LIST_FAIL)
        }

    @Suppress("UNCHECKED_CAST")
    fun getMatchDataList(matchIdList: List<String>, recentMatchId: String): List<HashMap<String, *>> =
        matchIdList.asSequence().filter { it > recentMatchId }.map {
            try {
                val uri = "$asiaBaseUri/match/v5/matches/$it"
                restTemplate.exchange(uri, HttpMethod.GET, httpEntity, HashMap::class.java).body!! as HashMap<String, *>
            } catch (ex: Exception) {
                throw CustomException(ErrorCode.GET_MATCH_DATA_FAIL)
            }
        }.toList()
}