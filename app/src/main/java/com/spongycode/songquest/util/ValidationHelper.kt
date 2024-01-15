package com.spongycode.songquest.util

import android.util.Patterns

object ValidationHelper {
    fun validateUsername(username: String): String? {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank()) {
            return "Username is required"
        }

        if (trimmedUsername.length < Constants.USERNAME_MIN_LENGTH) {
            return "Username too short"
        }

        if (trimmedUsername.length > Constants.USERNAME_MAX_LENGTH) {
            return "Username too long"
        }
        return null
    }

    fun validateEmail(email: String): String? {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()) {
            return "Email is required"
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            return "Email not valid"
        }
        return null
    }

    fun validateEmailOrUsername(emailOrUsername: String): String? {
        val trimmedEmailOrUsername = emailOrUsername.trim()
        if (trimmedEmailOrUsername.isBlank()) {
            return "Email or Password is required"
        }

        if (validateUsername(trimmedEmailOrUsername) != null &&
            validateEmail(trimmedEmailOrUsername) != null
        ) {
            return "Email or username is not valid"
        }
        return null
    }


    fun validatePassword(password: String): String? {
        val lettersInPassword = password.any { it.isLetter() }
        val numberInPassword = password.any { it.isDigit() }
        if (password.isBlank()) {
            return "Password is required"
        }
        if (!lettersInPassword) {
            return "Need a letter in password"
        }

        if (!numberInPassword) {
            return "Need a number in password"
        }
        if (password.length < 6) {
            return "Password too short"
        }
        return null
    }
}