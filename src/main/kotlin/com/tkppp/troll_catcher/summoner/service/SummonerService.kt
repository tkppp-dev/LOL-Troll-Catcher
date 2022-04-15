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
import java.net.URLEncoder

@Service
class SummonerService(
    private val summonerRepository: SummonerRepository,
    private val restTemplate: RestTemplate,
    private val httpEntity: HttpEntity<String>
) {

    fun getSummonerInfo(summonerName: String): String =
        when (val puuid = summonerRepository.findByName(summonerName)?.puuid) {
            null -> {
                try {
                    // restㄲemplate 은 uri를 자동으로 인코딩하기 때문에 먼저 할 필요 없다
                    val uri = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/$summonerName"
                    val body = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, HashMap::class.java).body!!
                    val entity = Summoner(puuid = body["puuid"] as String, name = body["name"] as String)
                    summonerRepository.save(entity).puuid
                } catch (ex: Exception) {
                    throw CustomException(ErrorCode.SUMMONER_DOESNT_EXIST)
                }
            }
            else -> puuid
        }

    fun getMatchDataList(puuid: String) {

    }
}