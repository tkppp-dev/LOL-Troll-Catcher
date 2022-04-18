package com.tkppp.troll_catcher.summoner.util

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class TrollScoreTest {

    @Test
    @DisplayName("트롤 수치 분포값 계산")
    fun getTrollScoreDistribution() {
        val results = mutableListOf<TrollScore>()
        for (death in 20 downTo 1) {
            for (kill in 0..death / 2) {
                for (assist in 0 until death ) {
                    results.add(TrollScore(kill, death, assist))
                }
            }
        }
        results.sortWith(compareByDescending { it.getTrollScore() })
        results.forEach { println("$it = ${it.getTrollScore()}")}
    }
}