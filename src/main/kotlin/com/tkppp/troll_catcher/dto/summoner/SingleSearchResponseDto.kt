package com.tkppp.troll_catcher.dto.summoner

import com.tkppp.troll_catcher.summoner.util.MatchInfo
import com.tkppp.troll_catcher.summoner.util.TrollPossibility

data class SingleSearchResponseDto(
    val duration: Int,
    val matchResult: Boolean,
    val champion: String,
    val position: String,
    val summonerSpell1Id: Int,
    val summonerSpell2Id: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val creepScore: Int,
    val trollPossibility: TrollPossibility
) {
    constructor(matchInfo: MatchInfo) : this(
        duration = matchInfo.duration,
        matchResult = matchInfo.matchResult,
        champion = matchInfo.champion,
        position = matchInfo.position,
        summonerSpell1Id = matchInfo.summonerSpell1Id,
        summonerSpell2Id = matchInfo.summonerSpell2Id,
        kills = matchInfo.kills,
        deaths = matchInfo.deaths,
        assists = matchInfo.assists,
        creepScore = matchInfo.creepScore,
        trollPossibility = matchInfo.trollPossibility
    )
}