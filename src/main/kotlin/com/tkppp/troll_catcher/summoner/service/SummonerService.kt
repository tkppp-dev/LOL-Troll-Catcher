package com.tkppp.troll_catcher.summoner.service

import com.tkppp.troll_catcher.exception.CustomException
import com.tkppp.troll_catcher.exception.ErrorCode
import com.tkppp.troll_catcher.summoner.domain.Summoner
import com.tkppp.troll_catcher.summoner.domain.SummonerRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
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
                val uri = "/summoner/v4/summoners/by-name/${URLEncoder.encode(summonerName, "UTF-8")}"
                val response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, HashMap::class.java)

                response.body?.let {
                    val entity = Summoner(puuid = it["puuid"] as String, name = it["name"] as String)
                    summonerRepository.save(entity).puuid
                } ?: throw CustomException(ErrorCode.SUMMONER_DOESNT_EXIST)
            }
            else -> puuid
        }

    fun getMatchDataList(puuid: String) {

    }
}