package com.tkppp.troll_catcher.summoner.domain

import org.springframework.data.jpa.repository.JpaRepository

interface SummonerRepository : JpaRepository<Summoner, Long> {

    fun findByName(summonerName: String): Summoner?
}