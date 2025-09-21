package com.spongycode.songquest.util

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

abstract class BasePreferenceManager {

    val settings = Settings()

    open fun clear() {
        settings.clear()
    }

    fun getStringOrNull(key: String): String? {
        return settings.getStringOrNull(key)
    }

    fun putBoolean(key: String, value: Boolean) {
        settings.putBoolean(key, value)
    }

    fun getBoolean(key: String) = settings.getBoolean(key, false)

    fun putString(key: String, value: String?) {
        value?.run {
            settings.putString(key, value)
        } ?: settings.remove(key)
    }

    fun getLongOrNull(key: String): Long? {
        return settings.getLongOrNull(key)
    }

    fun putLong(key: String, value: Long?) {
        value?.run {
            settings.putLong(key, value)
        } ?: settings.remove(key)
    }

    fun getIntOrNull(key: String): Int? {
        return settings.getIntOrNull(key)
    }

    fun putInt(key: String, value: Int?) {
        value?.run {
            settings.putInt(key, value)
        } ?: settings.remove(key)
    }

    fun getBooleanOrNull(key: String): Boolean? {
        return settings.getBooleanOrNull(key)
    }

    fun putBoolean(key: String, value: Boolean?) {
        value?.run {
            settings.putBoolean(key, value)
        } ?: settings.remove(key)
    }

    fun getFloatOrNull(key: String): Float? {
        return settings.getFloatOrNull(key)
    }

    fun putFloat(key: String, value: Float?) {
        value?.run {
            settings.putFloat(key, value)
        } ?: settings.remove(key)
    }

    fun getDoubleOrNull(key: String): Double? {
        return settings.getDoubleOrNull(key)
    }

    fun putDouble(key: String, value: Double?) {
        value?.run {
            settings.putDouble(key, value)
        } ?: settings.remove(key)
    }

    inline fun <reified T> putObject(key: String, classObject: T?): Boolean {
        return try {
            val objectString = Json.encodeToString(classObject)
            settings.putString(key, objectString)
            true
        } catch (_: Exception) {
            false
        }
    }

    inline fun <reified T> getObject(key: String): T? {
        return try {
            val objectClassInstance = Json.decodeFromString<T>(settings.getString(key, ""))
            objectClassInstance
        } catch (e: Exception) {
            null
        }
    }

    fun removeObject(key: String) {
        settings.remove(key)
    }
}