package com.tkppp.troll_catcher.summoner.controller

import com.tkppp.troll_catcher.dto.summoner.SingleSearchResponseDto
import com.tkppp.troll_catcher.summoner.service.SummonerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SummonerApiController(
    private val summonerService: SummonerService
) {

    @GetMapping("/search/single")
    fun returnSingleSearchResult(@RequestParam summonerName: String): ResponseEntity<List<SingleSearchResponseDto>> {
        val dto = summonerService.getSingleSearchResult(summonerName)
        return ResponseEntity(dto, HttpStatus.OK)
    }
}