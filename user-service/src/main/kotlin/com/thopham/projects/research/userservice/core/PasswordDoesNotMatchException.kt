package com.thopham.projects.research.userservice.core

class PasswordDoesNotMatchException: Exception {
    constructor(): super(
            "Password doesn't match"
    )
}