package com.tkppp.troll_catcher.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String,
) {
    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "요청 처리중 예상치 못한 에러가 발생했습니다")
}