package com.tkppp.troll_catcher.exception

class CustomException(val error: ErrorCode) : RuntimeException() {
}