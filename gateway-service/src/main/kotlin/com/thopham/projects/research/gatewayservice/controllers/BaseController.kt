package com.thopham.projects.research.gatewayservice.controllers

import io.grpc.Status
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

data class ErrorResponse(
    val status: Int,
    val message: String
)

@ControllerAdvice
class BaseController {
    @ExceptionHandler(value = [Exception::class])
    fun handleException(exception: Exception): ResponseEntity<ErrorResponse>{
        val description = Status.fromThrowable(exception)
            .description
        return ResponseEntity(
            ErrorResponse(
                status = 500,
                message = description ?: "UNKNOWN ERROR..."
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}