package com.tkppp.troll_catcher.summoner.util

data class TrollScore(
    private val kill: Int,
    private val death: Int,
    private val assist: Int,
) {

    private var kdRatio: Float = if (death == 0) 1F else kill.toFloat() / death.toFloat()
    private var kda: Float = if (death == 0) 1F else (kill + assist).toFloat() / death.toFloat()

    private fun getTrollScoreFromKDRatioWhenDeathMoreThan8(): Int {
        return if (kdRatio == 0F) 35
        else if (kdRatio <= 0.1) 32
        else if (kdRatio <= 0.125) 30
        else if (kdRatio <= 0.25) 25
        else if (kdRatio <= 0.375) 20
        else if (kdRatio <= 0.5) 15
        else 0
    }

    private fun getTrollScoreFromKDRatioWhenDeathMoreThan6(): Int {
        return if (kdRatio == 0F) 27
        else if (kdRatio <= 0.167) 24
        else if (kdRatio <= 0.333) 21
        else if (kdRatio <= 0.5) 15
        else 0
    }

    private fun getTrollScoreFromKDRatioWhenDeathMoreThan4(): Int {
        return if (kdRatio == 0F) 20
        else if (kdRatio <= 0.2) 15
        else if (kdRatio <= 0.25) 10
        else if (kdRatio <= 0.4) 5
        else 0
    }

    private fun getTrollScoreFromKDRatioWhenDeathLessThan3(): Int {
        return if (kdRatio == 0F) 10
        else if (kdRatio <= 0.334) 7
        else if (kdRatio <= 0.5) 4
        else 0
    }

    private fun getTrollScoreFromKDRatio(): Int =
        when (death) {
            in 0..3 -> getTrollScoreFromKDRatioWhenDeathLessThan3()
            in 4..5 -> getTrollScoreFromKDRatioWhenDeathMoreThan4()
            in 6..7 -> getTrollScoreFromKDRatioWhenDeathMoreThan6()
            else -> getTrollScoreFromKDRatioWhenDeathMoreThan8()
        }

    private fun getTrollScoreFromAssistWhenDeathLessThan3(): Int =
        when (assist) {
            0 -> 5
            1 -> 4
            2 -> 3
            3 -> 2
            4 -> 1
            else -> 0
        }

    private fun getTrollScoreFromAssistWhenDeathMoreThan4(): Int =
        when (assist) {
            0 -> 15
            1 -> 10
            2 -> 7
            3 -> 5
            4 -> 4
            5 -> 3
            6 -> 2
            7 -> 1
            else -> 0
        }

    private fun getTrollScoreFromAssist(): Int =
        when (death) {
            in 0..3 -> getTrollScoreFromAssistWhenDeathLessThan3()
            else -> getTrollScoreFromAssistWhenDeathMoreThan4()
        }

    private fun getTrollScoreFromKDAWhenDeathMoreThan10(): Int =
        if (kda == 0F) 30
        else if (kda <= 0.2) 28
        else if (kda <= 0.4) 26
        else if (kda <= 0.6) 24
        else if (kda <= 0.8) 22
        else if (kda <= 1) 20
        else 0

    private fun getTrollScoreFromKDAWhenDeathMoreThan6(): Int =
        if (kda == 0F) 30
        else if (kda <= 0.1) 25
        else if (kda <= 0.2) 20
        else if (kda <= 0.3) 15
        else if (kda <= 0.4) 10
        else if (kda <= 0.5) 5
        else 0

    private fun getTrollScoreFromKDAWhenDeathMoreThan4(): Int =
        if (kda == 0F) 20
        else if (kda <= 0.1) 17
        else if (kda <= 0.2) 14
        else if (kda <= 0.3) 11
        else if (kda <= 0.4) 8
        else if (kda <= 0.5) 5
        else 0

    private fun getTrollScoreFromKDAWhenDeathLessThan4(): Int =
        if (kda == 0F) 15
        else if (kda <= 0.1) 12
        else if (kda <= 0.2) 9
        else if (kda <= 0.3) 6
        else if (kda <= 0.4) 3
        else 0

    private fun getTrollScoreFromKDA(): Int =
        when (death) {
            in 0..3 -> getTrollScoreFromKDAWhenDeathLessThan4()
            in 4..5 -> getTrollScoreFromKDAWhenDeathMoreThan4()
            in 6..9 -> getTrollScoreFromKDAWhenDeathMoreThan6()
            else -> getTrollScoreFromKDAWhenDeathMoreThan10()
        }

    fun getTrollScore(): Int = getTrollScoreFromKDRatio() + getTrollScoreFromAssist() + getTrollScoreFromKDA()
}