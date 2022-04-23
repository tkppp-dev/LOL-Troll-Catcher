package com.tkppp.troll_catcher.summoner.util

data class MatchInfo(
    val matchId: String,
    val matchResult: Boolean,
    val duration: Int,
    val champion: String,
    val position: String,
    val summonerSpell1Id: Int,
    val summonerSpell2Id : Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val creepScore: Int,
) {
    lateinit var trollPossibility: TrollPossibility
}