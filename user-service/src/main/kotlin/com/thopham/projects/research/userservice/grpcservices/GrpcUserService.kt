package com.thopham.projects.research.userservice.grpcservices

import com.thopham.projects.dimohi.userservice.grpcentities.*
import com.thopham.projects.research.userservice.core.observe
import com.thopham.projects.research.userservice.services.LoginUserParams
import com.thopham.projects.research.userservice.services.RegisterUserParams
import com.thopham.projects.research.userservice.services.UserService
import io.grpc.stub.StreamObserver
import org.springframework.stereotype.Component

@Component
class GrpcUserServiceImpl(private val userService: UserService): UserServiceGrpc.UserServiceImplBase() {
    override fun register(request: RegisterUserParamsProtobuf, responseObserver: StreamObserver<RegisterUserResultProtobuf>) {
        val params = RegisterUserParams(
            request.phone,
            request.email,
            request.password
        )
        userService.register(params)
            .map {result ->
                val user = result.user
                RegisterUserResultProtobuf.newBuilder()
                    .setUser(
                        UserProtobuf.newBuilder()
                            .setId(user.id)
                            .setEmail(user.email)
                            .setPhone(user.phone)
                            .build()
                    )
                    .build()
            }
            .observe(responseObserver)
    }

    override fun login(request: LoginUserParamsProtobuf, responseObserver: StreamObserver<LoginUserResultProtobuf>) {
        val params = LoginUserParams(
            request.phone,
            request.password
        )
        userService.login(params)
            .map {result ->
                val user = result.user
                LoginUserResultProtobuf.newBuilder()
                    .setUser(
                        UserProtobuf.newBuilder()
                            .setId(user.id)
                            .setEmail(user.email)
                            .setPhone(user.phone)
                            .build()
                    )
                    .build()
            }
            .observe(responseObserver)
    }
}