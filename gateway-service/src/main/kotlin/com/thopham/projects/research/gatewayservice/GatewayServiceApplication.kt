package com.thopham.projects.research.gatewayservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GatewayServiceApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<GatewayServiceApplication>(*args)
        }
    }
}


