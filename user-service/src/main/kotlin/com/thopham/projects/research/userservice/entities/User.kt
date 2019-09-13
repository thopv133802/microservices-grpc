package com.thopham.projects.research.userservice.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "users")
data class User(
        @Id
        val id: String,
        val phone: String,
        val email: String,
        val password: String
) {
        constructor(phone: String, email: String, password: String):
                this(id = UUID.randomUUID().toString(), phone = phone, email = email, password = password)

        fun matches(otherPassword: String): Boolean {
                return password == otherPassword
        }
}