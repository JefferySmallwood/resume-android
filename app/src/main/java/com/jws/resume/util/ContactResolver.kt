package com.jws.resume.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.jws.resume.R

sealed class ContactType(
    open val userText: String,
    @field:DrawableRes open val iconRes: Int = R.drawable.baseline_call_24,
) {
    sealed class Web(
        open val url: String,
        override val userText: String,
        @field:DrawableRes override val iconRes: Int,
        @field:StringRes open val invalidUrl: Int,
        @field:StringRes open val linkError: Int,
    ): ContactType(userText, iconRes) {
        data class DEFAULT(
            override val url: String,
            override val userText: String = url,
            @field:DrawableRes override val iconRes: Int = R.drawable.baseline_link_24,
            @field:StringRes override val invalidUrl: Int = R.string.invalid_url_format,
            @field:StringRes override val linkError: Int = R.string.could_not_open_link,
        ) : Web(userText, url, iconRes, invalidUrl, linkError)
        data class LINKEDIN(
            override val url: String,
            override val userText: String = url,
            @field:DrawableRes override val iconRes: Int = R.drawable.inbug_white,
            @field:StringRes override val invalidUrl: Int = R.string.invalid_url_format,
            @field:StringRes override val linkError: Int = R.string.could_not_open_link,
        ) : Web(userText, url, iconRes, invalidUrl, linkError)
        data class GITHUB(
            override val url: String,
            override val userText: String = url,
            @field:DrawableRes override val iconRes: Int = R.drawable.github_mark_white,
            @field:StringRes override val invalidUrl: Int = R.string.invalid_url_format,
            @field:StringRes override val linkError: Int = R.string.could_not_open_link,
        ) : Web(userText, url, iconRes, invalidUrl, linkError)
    }
    data class EMAIL(
        val emailAddress: String,
        override val userText: String = emailAddress,
        val subject: String = "",
        @field:DrawableRes override val iconRes: Int = R.drawable.baseline_email_24,
        @field:StringRes val noActivityFound: Int = R.string.no_email_application_found
    ) : ContactType(userText, iconRes)
    data class PHONE(
        val phoneNumber: String,
        override val userText: String = phoneNumber,
        @field:DrawableRes override val iconRes: Int = R.drawable.baseline_call_24,
        @field:StringRes val noActivityFound: Int = R.string.no_application_found_to_make_calls
    ) : ContactType(userText, iconRes)
}

object ContactResolver {

    fun resolveContact(context: Context, contactType: ContactType) {
        when (contactType) {
            is ContactType.Web -> openUrlInCustomTab(context, contactType)
            is ContactType.PHONE -> openPhoneDialer(context, contactType)
            is ContactType.EMAIL -> openEmailClient(context, contactType)
        }
    }

    private fun openUrlInCustomTab(
        context: Context,
        webLink: ContactType.Web
    ) {
        try {
            val customTabsIntent = CustomTabsIntent.Builder().build()

            val uri = webLink.url.toUri()
            if (uri.scheme == null || (!uri.scheme.equals("http") && !uri.scheme.equals("https"))) {
                ToastHelper.showUniqueToast(context, resId = webLink.invalidUrl, Toast.LENGTH_SHORT)
                return
            }

            customTabsIntent.launchUrl(context, uri)
        } catch (e: Exception) {
            ToastHelper.showUniqueToast(context, resId = webLink.linkError, duration = Toast.LENGTH_SHORT)
        }
    }

    private fun openEmailClient(
        context: Context,
        email: ContactType.EMAIL
    ) {
        val encodedEmail = Uri.encode(email.emailAddress)
        val encodedSubject = Uri.encode(email.subject)

        val mailtoUri = "mailto:$encodedEmail?subject=$encodedSubject".toUri()
        val emailIntent = Intent(Intent.ACTION_SENDTO, mailtoUri)


        if (emailIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(emailIntent)
        } else {
            ToastHelper.showUniqueToast(context, resId = email.noActivityFound, duration = Toast.LENGTH_SHORT)
        }
    }

    private fun openPhoneDialer(
        context: Context,
        phone: ContactType.PHONE
    ) {
        val encodedPhoneNumber = Uri.encode(phone.phoneNumber)
        val dialIntent = Intent(Intent.ACTION_DIAL, "tel:$encodedPhoneNumber".toUri())

        if (dialIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(dialIntent)
        } else {
            ToastHelper.showUniqueToast(context, resId = phone.noActivityFound, duration = Toast.LENGTH_SHORT)
        }
    }
}

