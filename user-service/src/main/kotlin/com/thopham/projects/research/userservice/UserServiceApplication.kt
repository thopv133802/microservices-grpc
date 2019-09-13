package com.thopham.projects.research.userservice

import com.thopham.projects.research.userservice.grpcservices.GrpcUserServiceImpl
import com.thopham.projects.research.userservice.services.UserService
import io.grpc.ManagedChannelBuilder
import io.grpc.ServerBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserServiceApplication {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			val context = runApplication<UserServiceApplication>(*args)
			val port = context.environment.getProperty("server.port")?.toInt() ?: 8080
			val userService = context.getBean(GrpcUserServiceImpl::class.java)
			val server = ServerBuilder.forPort(port)
				.addService(
					userService
				)
				.build()
				.start()
			Runtime.getRuntime()
				.addShutdownHook(Thread {
					server.shutdown()
				})
			server.awaitTermination()
			println("Listen on port: $port")
		}
	}
}

