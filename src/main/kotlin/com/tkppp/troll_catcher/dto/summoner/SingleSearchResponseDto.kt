package com.tkppp.troll_catcher.dto.summoner

import com.tkppp.troll_catcher.summoner.util.MatchInfo

data class SingleSearchResponseDto(
    val duration: Int,
    val champion: String,
    val position: String,
    val summonerSpell1Id: Int,
    val summonerSpell2Id: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val creepScore: Int
) {
    constructor(matchInfo: MatchInfo) : this(
        duration = matchInfo.duration,
        champion = matchInfo.champion,
        position = matchInfo.position,
        summonerSpell1Id = matchInfo.summonerSpell1Id,
        summonerSpell2Id = matchInfo.summonerSpell2Id,
        kills = matchInfo.kills,
        deaths = matchInfo.deaths,
        assists = matchInfo.assists,
        creepScore = matchInfo.creepScore
    )
}