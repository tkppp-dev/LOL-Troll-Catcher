package com.tkppp.troll_catcher.summoner.service

import com.tkppp.troll_catcher.exception.CustomException
import com.tkppp.troll_catcher.exception.ErrorCode
import com.tkppp.troll_catcher.summoner.domain.Summoner
import com.tkppp.troll_catcher.summoner.domain.SummonerRepository
import com.tkppp.troll_catcher.summoner.util.AnalysisResult
import com.tkppp.troll_catcher.summoner.util.ResultType
import com.tkppp.troll_catcher.summoner.util.TrollScore
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

    fun getSingleSearchResult(summonerName: String) {
        val summoner = getSummonerInfo(summonerName)
        val puuid = summoner.puuid

        val matchIdList = getMatchIdList(puuid)
        val matchDataList = getMatchDataList(matchIdList, summoner.recentMatchId)
        val scoreList = getTrollScoreList(matchDataList, puuid)

        val analysisResults = (scoreList + (summoner.analysisResults ?: listOf())).let { results ->
            if (results.size > 30) results.subList(0, 30) else results
        }
        summoner.analysisResults = analysisResults
        matchIdList[0].let { matchId -> summoner.recentMatchId = matchId }
        summonerRepository.save(summoner)

        val analysisAtRecent10Game = AnalysisResult(ResultType.Recent10Games)
        val analysisAtRecent30Game = AnalysisResult(ResultType.Recent30Games)

        for((idx, trollScore) in analysisResults.withIndex()){
            if(idx < 10) analysisAtRecent10Game.judgePossibility(trollScore)
            analysisAtRecent30Game.judgePossibility(trollScore)
        }
    }

    fun getSummonerInfo(summonerName: String): Summoner {
        val summoner = summonerRepository.findByName(summonerName)
        return when (summoner?.puuid) {
            null -> {
                try {
                    // restTemplate 은 uri 를 자동으로 인코딩하기 때문에 따로 할 필요 없다
                    val uri = "$krBaseUri/summoner/v4/summoners/by-name/$summonerName"
                    val body = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, HashMap::class.java).body!!
                    Summoner(puuid = body["puuid"] as String, name = body["name"] as String)
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
            val param = "type=ranked&start=0&count=30"
            val uri = "$asiaBaseUri/match/v5/matches/by-puuid/${puuid}/ids?$param"
            restTemplate.exchange(uri, HttpMethod.GET, httpEntity, List::class.java).body!! as List<String>
        } catch (ex: Exception) {
            throw CustomException(ErrorCode.GET_MATCH_ID_LIST_FAIL)
        }

    @Suppress("UNCHECKED_CAST")
    fun getMatchDataList(matchIdList: List<String>, recentMatchId: String?): List<HashMap<String, *>> =
        matchIdList.asSequence().filter { it > (recentMatchId ?: "KR_0") }.map {
            try {
                val uri = "$asiaBaseUri/match/v5/matches/$it"
                restTemplate.exchange(uri, HttpMethod.GET, httpEntity, HashMap::class.java).body!! as HashMap<String, *>
            } catch (ex: Exception) {
                throw CustomException(ErrorCode.GET_MATCH_DATA_FAIL)
            }
        }.toList()

    @Suppress("UNCHECKED_CAST")
    fun getTrollScoreList(matchDataList: List<HashMap<String, *>>, puuid: String): List<Int> =
        matchDataList.map { matchData ->
            val info = matchData["info"] as HashMap<String, *>
            val personalDataList = info["participants"] as List<HashMap<String, *>>
            val personalData = personalDataList.groupBy { it["puuid"] }[puuid]!![0]
            TrollScore(
                personalData["kills"] as Int,
                personalData["deaths"] as Int,
                personalData["assists"] as Int
            ).getTrollScore()
        }
}