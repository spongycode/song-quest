package com.spongycode.songquest.util

import android.util.Patterns

object ValidationHelper {
    fun validateFullName(fullName: String): String? {
        val trimmedFullName = fullName.trim()
        if (trimmedFullName.isBlank()) {
            return "Name is required"
        }
        return null
    }

    fun validateUsername(username: String): String? {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank()) {
            return "Username is required"
        }

        if (trimmedUsername.length < 4) {
            return "Username too short"
        }
        return null
    }

    fun validateEmail(email: String): String? {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()) {
            return "Email is required"
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email not valid"
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