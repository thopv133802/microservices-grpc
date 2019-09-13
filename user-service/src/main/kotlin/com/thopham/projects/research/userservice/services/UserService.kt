package com.thopham.projects.research.userservice.services

import com.thopham.projects.research.userservice.core.PasswordDoesNotMatchException
import com.thopham.projects.research.userservice.core.UserAlreadyExistsException
import com.thopham.projects.research.userservice.core.UserDoesNotExistsException
import com.thopham.projects.research.userservice.entities.User
import com.thopham.projects.research.userservice.repositories.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

data class RegisterUserParams(
    val phone: String,
    val email: String,
    val password: String
)

data class RegisterUserResult(
    val user: User
)

data class LoginUserParams(
    val phone: String,
    val password: String
)

data class LoginUserResult(
    val user: User
)

interface UserService {
    fun register(params: RegisterUserParams): Mono<RegisterUserResult>
    fun login(params: LoginUserParams): Mono<LoginUserResult>
}

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun register(params: RegisterUserParams): Mono<RegisterUserResult> {
        return userRepository.existsByPhone(params.phone)
            .filter { exists ->
                !exists
            }
            .switchIfEmpty(
                Mono.error(
                    UserAlreadyExistsException(
                        params.phone
                    )
                )
            )
            .flatMap {
                userRepository.save(
                    User(
                        phone = params.phone,
                        email = params.email,
                        password = params.password
                    )
                )
            }
            .map { user ->
                RegisterUserResult(
                    user = user
                )
            }

    }

    override fun login(params: LoginUserParams): Mono<LoginUserResult> {
        return userRepository.existsByPhone(params.phone)
            .filter { exists ->
                exists
            }
            .switchIfEmpty(
                Mono.error(
                    UserDoesNotExistsException(
                        params.phone
                    )
                )
            )
            .flatMap {
                userRepository.findByPhone(params.phone)
            }
            .filter { user ->
                user.matches(params.password)
            }
            .switchIfEmpty(
                Mono.error(PasswordDoesNotMatchException())
            )
            .map { user ->
                LoginUserResult(
                    user
                )
            }
    }

}