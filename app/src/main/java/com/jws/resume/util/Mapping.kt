package com.jws.resume.util

@Suppress("UNCHECKED_CAST")
fun <T> List<*>.mapToTypedList(transform: (Map<String, Any>) -> T): List<T> {
    return this.mapNotNull { item ->
        if (item is Map<*, *>) {
            try {
                transform(item as Map<String, Any>)
            } catch (e: Exception) {
                println("Error mapping item: $item. Error: ${e.message}")
                null
            }
        } else {
            null
        }
    }
}

fun Map<String, Any>.getString(key: String): String? = this[key] as? String

fun Map<String, Any>.getInt(key: String): Int? {
    return when (val value = this[key]) {
        is Number -> value.toInt()
        is String -> value.toIntOrNull()
        else -> null
    }
}

@Suppress("UNCHECKED_CAST")
fun Map<String, Any>.getStringList(key: String): List<String>? {
    return (this[key] as? List<*>)?.mapNotNull { it as? String }
}