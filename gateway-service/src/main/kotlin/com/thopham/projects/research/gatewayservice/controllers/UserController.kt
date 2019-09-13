package com.thopham.projects.research.gatewayservice.controllers

import com.thopham.projects.dimohi.userservice.grpcentities.LoginUserParamsProtobuf
import com.thopham.projects.dimohi.userservice.grpcentities.LoginUserResultProtobuf
import com.thopham.projects.dimohi.userservice.grpcentities.RegisterUserParamsProtobuf
import com.thopham.projects.dimohi.userservice.grpcentities.RegisterUserResultProtobuf
import com.thopham.projects.research.gatewayservice.services.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/users")
class UserController(val userService: UserService){
    @PostMapping("/register")
    fun register(@RequestBody params: RegisterUserParams): Mono<RegisterUserResult> {
        return userService.register(params)
    }
    @PostMapping("/login")
    fun login(@RequestBody params: LoginUserParams): Mono<LoginUserResult>{
        return userService.login(params)
    }
}