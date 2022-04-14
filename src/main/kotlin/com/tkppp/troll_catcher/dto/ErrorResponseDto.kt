package com.tkppp.troll_catcher.dto

import org.springframework.http.HttpStatus

data class ErrorResponseDto(
    val status: HttpStatus,
    val message: String
)
