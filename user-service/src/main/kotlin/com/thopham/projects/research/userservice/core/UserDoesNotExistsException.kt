package com.thopham.projects.research.userservice.core

class UserDoesNotExistsException: Exception {
    constructor(phone: String): super(
            "User with $phone doesn't exists"
    )
}