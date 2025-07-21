package com.jws.resume.ui.access

import androidx.annotation.StringRes
import com.google.firebase.functions.FirebaseFunctionsException
import com.jws.resume.R


sealed class AccessCodeError(
    @field:StringRes val messageResId: Int,
    val formatArgs: Any? = null
) {
    data class EmptyNotAllowed(
        @field:StringRes val resId: Int = R.string.error_empty_not_allowed,
    ) : AccessCodeError(resId)
    data class InvalidArgument(
        @field:StringRes val resId: Int = R.string.error_invalid_request,
    ) : AccessCodeError(resId)
    data class NotFound(
        @field:StringRes val resId: Int = R.string.error_invalid_or_not_found
    ) : AccessCodeError(resId)
    data class PermissionDenied(
        @field:StringRes val resId: Int = R.string.error_access_denied,
    ) : AccessCodeError(resId)
    data class Internal(
        @field:StringRes val resId: Int = R.string.error_server,
    ) : AccessCodeError(resId)
    data class InvalidAccessOrPermission(
        @field:StringRes val resId: Int = R.string.error_invalid_access_or_permission,
    ) : AccessCodeError(resId)
    data class UnknownServer(
        @field:StringRes val resId: Int = R.string.error_unknown_server,
        val arg: String? = null
    ) : AccessCodeError(resId)
    data class UnknownError(
        @field:StringRes val resId: Int = R.string.error_unexpected,
        val arg: String? = null
    ) : AccessCodeError(resId, arg)
}


fun handleException(e: Exception): AccessCodeError {
    return when (e) {
        is FirebaseFunctionsException -> {
            when (e.code) {
                FirebaseFunctionsException.Code.INVALID_ARGUMENT -> AccessCodeError.InvalidArgument()
                FirebaseFunctionsException.Code.NOT_FOUND -> AccessCodeError.NotFound()
                FirebaseFunctionsException.Code.PERMISSION_DENIED -> AccessCodeError.PermissionDenied()
                FirebaseFunctionsException.Code.INTERNAL -> AccessCodeError.Internal()
                else -> AccessCodeError.UnknownServer(arg = e.code.toString())
            }
        }

        else -> {
            if (e.message?.contains(other = "Invalid access code.", ignoreCase = true) == true ||
                e.message?.contains(other = "Access code is required", ignoreCase = true) == true ||
                e.message?.contains(other = "Access code is inactive", ignoreCase = true) == true ||
                e.message?.contains(other = "Access code has no uses remaining", ignoreCase = true) == true ||
                e.message?.contains(other = "Access code has expired", ignoreCase = true) == true ||
                e.message?.contains(other = "Resume data not found for this user", ignoreCase = true) == true
            ) {
                AccessCodeError.InvalidAccessOrPermission()
            } else {
                AccessCodeError.UnknownError()
            }
        }
    }
}