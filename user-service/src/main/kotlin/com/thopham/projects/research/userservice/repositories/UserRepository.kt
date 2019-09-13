package com.thopham.projects.research.userservice.repositories

import com.thopham.projects.research.userservice.entities.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository: ReactiveMongoRepository<User, String> {
    fun findByPhone(phone: String): Mono<User>
    fun existsByPhone(phone: String): Mono<Boolean>
}