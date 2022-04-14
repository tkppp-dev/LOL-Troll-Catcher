package com.tkppp.troll_catcher.exception

import com.tkppp.troll_catcher.dto.ErrorResponseDto
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler::class.java)

    @ExceptionHandler(value = [CustomException::class])
    fun handlingCustomException(ex: CustomException): ResponseEntity<ErrorResponseDto> {
        val statusCode = ex.error.status
        val errorMsg = ex.error.message

        logger.error("${ex.error.name} - ${ex.message}", ex)
        return ResponseEntity(ErrorResponseDto(statusCode, errorMsg), statusCode)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handlingUnexpectedException(ex: Exception): ResponseEntity<ErrorResponseDto> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        logger.error("${ex.message}", ex)
        return ResponseEntity(ErrorResponseDto(errorCode.status, errorCode.message), errorCode.status)
    }
}