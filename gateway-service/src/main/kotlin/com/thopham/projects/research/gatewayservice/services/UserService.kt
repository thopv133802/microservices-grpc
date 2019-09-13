package com.thopham.projects.research.gatewayservice.services

import com.thopham.projects.dimohi.userservice.grpcentities.*
import com.thopham.projects.research.gatewayservice.core.createStreamObserver
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoProcessor

data class RegisterUserParams(
    val phone: String,
    val email: String,
    val password: String
) {
    fun toProtobuf(): RegisterUserParamsProtobuf {
        return RegisterUserParamsProtobuf.newBuilder()
            .setPhone(phone)
            .setEmail(email)
            .setPassword(password)
            .build()
    }
}

data class RegisterUserResult(
    val user: User
) {
    companion object {
        fun fromProtobuf(protobuf: RegisterUserResultProtobuf): RegisterUserResult {
            return RegisterUserResult(
                user = User(
                    id = protobuf.user.id,
                    phone = protobuf.user.phone,
                    email = protobuf.user.email
                )
            )
        }
    }
}

data class LoginUserParams(
    val phone: String,
    val password: String
) {
    fun toProtobuf(): LoginUserParamsProtobuf {
        return LoginUserParamsProtobuf.newBuilder()
            .setPhone(phone)
            .setPassword(password)
            .build()
    }
}

data class LoginUserResult(
    val user: User
) {
    companion object {
        fun fromProtobuf(protobuf: LoginUserResultProtobuf): LoginUserResult {
            val user = protobuf.user
            return LoginUserResult(
                user = User(
                    id = user.id,
                    phone = user.phone,
                    email = user.email
                )
            )
        }
    }
}

data class User(
    val id: String,
    val phone: String,
    val email: String
    )

interface UserService {
    fun register(params: RegisterUserParams): Mono<RegisterUserResult>
    fun login(params: LoginUserParams): Mono<LoginUserResult>
}

@Component
class UserServiceImpl: UserService {
    @Value("\${user-service.host}")
    lateinit var host: String
    @Value("\${user-service.port}")
    var port: Int = 0
    private fun newChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext()
            .build()
    }
    override fun register(params: RegisterUserParams): Mono<RegisterUserResult> {
        val channel = newChannel()
        val stub = UserServiceGrpc.newStub(channel)
        val processor = MonoProcessor.create<RegisterUserResultProtobuf>()

        stub.register(
            params.toProtobuf(),
            processor.createStreamObserver()
        )

        return processor.doOnTerminate {
            channel.shutdown()
        }
            .map { protobuf ->
                RegisterUserResult.fromProtobuf(protobuf)
            }
    }

    override fun login(params: LoginUserParams): Mono<LoginUserResult> {
        val channel = newChannel()
        val stub = UserServiceGrpc.newStub(channel)
        val processor = MonoProcessor.create<LoginUserResultProtobuf>()

        stub.login(
            params.toProtobuf(),
            processor.createStreamObserver()
        )
        return processor.doOnTerminate {
            channel.shutdown()
        }
            .map { protobuf ->
                LoginUserResult.fromProtobuf(protobuf)
            }
    }

}

