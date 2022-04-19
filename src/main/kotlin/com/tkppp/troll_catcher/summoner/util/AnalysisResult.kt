package com.tkppp.troll_catcher.summoner.util

import kotlin.math.roundToInt

class AnalysisResult(
    val resultType: ResultType,
    var veryHigh: Int = 0,
    var high: Int = 0,
    var opacity: Int = 0,
    var noPossibility: Int = 0,
    var totalGames: Int = 0
) {
    lateinit var mentalState: MentalState

    fun judgePossibility(score: Int) {
        when (score) {
            in 0..20 -> noPossibility++
            in 21..35 -> opacity++
            in 36..50 -> high++
            else -> veryHigh++
        }
    }

    fun analysisPossibility(type: ResultType){
        val totalGames = veryHigh + high + opacity + noPossibility
        val percentage = ((veryHigh + high)/(totalGames.toFloat()) * 100).roundToInt()
        mentalState = when(percentage){
            in 50..100 -> MentalState.WORST
            in 30..49 -> MentalState.NOT_GOOD
            in 1..29 -> MentalState.NORMAL
            else -> MentalState.VERY_GOOD
        }
    }
}