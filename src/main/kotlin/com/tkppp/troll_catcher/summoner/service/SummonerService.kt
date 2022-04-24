package com.tkppp.troll_catcher.summoner.service

import com.tkppp.troll_catcher.dto.summoner.SingleSearchResponseDto
import com.tkppp.troll_catcher.exception.CustomException
import com.tkppp.troll_catcher.exception.ErrorCode
import com.tkppp.troll_catcher.summoner.domain.Summoner
import com.tkppp.troll_catcher.summoner.domain.SummonerRepository
import com.tkppp.troll_catcher.summoner.util.MatchInfo
import com.tkppp.troll_catcher.summoner.util.TrollPossibility.*
import com.tkppp.troll_catcher.summoner.util.TrollScore
import kotlinx.coroutines.*
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SummonerService(
    private val summonerRepository: SummonerRepository,
    private val restTemplate: RestTemplate,
    private val httpEntity: HttpEntity<String>
) {

    private val krBaseUri = "https://kr.api.riotgames.com/lol"
    private val asiaBaseUri = "https://asia.api.riotgames.com/lol"

    fun getSingleSearchResult(summonerName: String): List<SingleSearchResponseDto> {
        var summoner = getSummonerInfo(summonerName)
        val puuid = summoner.puuid

        val matchIdList = getMatchIdList(puuid)
        val matchInfoList = getMatchInfoList(matchIdList, summoner.recentMatchId, puuid)
        if (matchInfoList.isNotEmpty()) {
            summoner.matchInfos = (matchInfoList + (summoner.matchInfos ?: listOf())).let {
                if (it.size > 30) it.subList(0, 30) else it
            }
            summoner.recentMatchId = matchIdList.first()
            summoner = summonerRepository.save(summoner)
        }

        return summoner.matchInfos?.map { SingleSearchResponseDto(it) } ?: listOf()
    }

    fun getSingleSearchResultNonBlocking(summonerName: String): List<SingleSearchResponseDto> {
        var summoner = getSummonerInfo(summonerName)
        val puuid = summoner.puuid

        val matchIdList = getMatchIdList(puuid)
        val matchInfoList = getMatchInfoListNonBlocking(matchIdList, summoner.recentMatchId, puuid)
        if (matchInfoList.isNotEmpty()) {
            summoner.matchInfos = (matchInfoList + (summoner.matchInfos ?: listOf())).let {
                if (it.size > 30) it.subList(0, 30) else it
            }
            summoner.recentMatchId = matchIdList.first()
            summoner = summonerRepository.save(summoner)
        }

        return summoner.matchInfos?.map { SingleSearchResponseDto(it) } ?: listOf()
    }

    fun getSummonerInfo(summonerName: String): Summoner {
        val summoner = summonerRepository.findByName(summonerName)
        return when (summoner?.puuid) {
            null -> {
                try {
                    // restTemplate 은 uri 를 자동으로 인코딩하기 때문에 따로 할 필요 없다
                    val uri = "$krBaseUri/summoner/v4/summoners/by-name/$summonerName"
                    val body = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, HashMap::class.java).body!!
                    summonerRepository.findByPuuid(body["puuid"] as String)?.let {
                        it.name = body["name"] as String
                        it
                    } ?: Summoner(puuid = body["puuid"] as String, name = body["name"] as String)
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
    fun getMatchInfoList(matchIdList: List<String>, recentMatchId: String?, puuid: String): List<MatchInfo> =
        matchIdList.asSequence().filter { it > (recentMatchId ?: "KR_0") }.map { matchId ->
            try {
                val uri = "$asiaBaseUri/match/v5/matches/$matchId"
                val body = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    httpEntity,
                    HashMap::class.java
                ).body!! as HashMap<String, *>
                val info = body["info"] as HashMap<String, *>
                val personalDataList = info["participants"] as List<HashMap<String, *>>
                val personalData = personalDataList.groupBy { it["puuid"] }[puuid]!![0]
                val matchInfo = MatchInfo(
                    matchId = matchId,
                    matchResult = personalData["win"] as Boolean,
                    duration = info["gameDuration"] as Int,
                    champion = personalData["championName"] as String,
                    position = personalData["teamPosition"] as String,
                    summonerSpell1Id = personalData["summoner1Id"] as Int,
                    summonerSpell2Id = personalData["summoner2Id"] as Int,
                    kills = personalData["kills"] as Int,
                    deaths = personalData["deaths"] as Int,
                    assists = personalData["assists"] as Int,
                    creepScore = personalData["totalMinionsKilled"] as Int + personalData["neutralMinionsKilled"] as Int
                )
                matchInfo.trollPossibility =
                    when (TrollScore(matchInfo.kills, matchInfo.deaths, matchInfo.assists).getTrollScore()) {
                        in 0..20 -> NO_POSSIBILITY
                        in 21..35 -> OPACITY
                        in 36..50 -> HIGH
                        else -> VERY_HIGH
                    }
                matchInfo
            } catch (ex: Exception) {
                throw CustomException(ErrorCode.GET_MATCH_DATA_FAIL)
            }
        }.toList()

    suspend fun getMatchInfo(uri: String): ResponseEntity<*> = withContext(Dispatchers.IO) {
        try {
            restTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                HashMap::class.java
            )
        } catch (ex: Exception) {
            when(ex.message?.let { it.split(" ")[0] }){
                "429" -> {
                    delay(1000L)
                    getMatchInfo(uri)
                }
                else -> {
                    ex.printStackTrace()
                    throw CustomException(ErrorCode.GET_MATCH_DATA_FAIL)
                }
            }

        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getMatchInfoListNonBlocking(
        matchIdList: List<String>,
        recentMatchId: String?,
        puuid: String
    ): List<MatchInfo> = runBlocking {
        val deferredArray = matchIdList.asSequence().filter { it > (recentMatchId ?: "KR_0") }.map { matchId ->
            CoroutineScope(Dispatchers.IO).async {
                val uri = "$asiaBaseUri/match/v5/matches/$matchId"
                val body = getMatchInfo(uri).body!! as HashMap<String, *>
                val info = body["info"] as HashMap<String, *>
                val personalDataList = info["participants"] as List<HashMap<String, *>>
                val personalData = personalDataList.groupBy { it["puuid"] }[puuid]!![0]
                val matchInfo = MatchInfo(
                    matchId = matchId,
                    matchResult = personalData["win"] as Boolean,
                    duration = info["gameDuration"] as Int,
                    champion = personalData["championName"] as String,
                    position = personalData["teamPosition"] as String,
                    summonerSpell1Id = personalData["summoner1Id"] as Int,
                    summonerSpell2Id = personalData["summoner2Id"] as Int,
                    kills = personalData["kills"] as Int,
                    deaths = personalData["deaths"] as Int,
                    assists = personalData["assists"] as Int,
                    creepScore = personalData["totalMinionsKilled"] as Int + personalData["neutralMinionsKilled"] as Int
                )
                matchInfo.trollPossibility =
                    when (TrollScore(matchInfo.kills, matchInfo.deaths, matchInfo.assists).getTrollScore()) {
                        in 0..20 -> NO_POSSIBILITY
                        in 21..35 -> OPACITY
                        in 36..50 -> HIGH
                        else -> VERY_HIGH
                    }
                matchInfo
            }
        }.toList().toTypedArray()
        awaitAll(*deferredArray)
    }
}