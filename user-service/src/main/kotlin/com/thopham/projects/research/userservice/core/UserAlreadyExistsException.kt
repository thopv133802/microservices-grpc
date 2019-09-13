package com.thopham.projects.research.userservice.core

class UserAlreadyExistsException: Exception {
    constructor(phone: String): super(
            "Phone $phone already in use"
    )
}