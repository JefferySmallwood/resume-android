package com.jws.resume.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.jws.resume.ui.theme.ResumeTheme
import com.jws.resume.util.ContactResolver
import com.jws.resume.util.ContactType

@Composable
fun ContactItem(
    contactType: ContactType,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface
) {
    val context = LocalContext.current
    TextIcon(
        text = contactType.userText,
        modifier = Modifier
            .clickable { ContactResolver.resolveContact(context, contactType) }
            .then(modifier),
        start = IconInfo(iconRes = contactType.iconRes),
        iconTint = iconTint,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.secondary,
        ),
    )
}

@Preview(showBackground = true, name = "Contact Item (Default Web) Preview")
@Composable
fun ContactItemWebDefaultPreview() {
    ResumeTheme {
        ContactItem(contactType = ContactType.Web.DEFAULT(url = "https://www.example.com"))
    }
}

@Preview(showBackground = true, name = "Contact Item (Web LinkedIn) Preview")
@Composable
fun ContactItemWebLinkedInPreview() {
    ResumeTheme {
        ContactItem(contactType = ContactType.Web.LINKEDIN(url = "https://www.linkedin.com"))
    }
}

@Preview(showBackground = true, name = "Contact Item (Web GitHub) Preview")
@Composable
fun ContactItemWebGitHubPreview() {
    ResumeTheme {
        ContactItem(contactType = ContactType.Web.GITHUB(url = "https://www.github.com"))
    }
}

@Preview(showBackground = true, name = "Contact Item (Email) Preview")
@Composable
fun ContactItemEmailPreview() {
    ResumeTheme {
        ContactItem(
            contactType = ContactType.EMAIL(
                emailAddress = "william.henry.harrison@example-pet-store.com",
                userText = "Email address",
            )
        )
    }
}

@Preview(showBackground = true, name = "Contact Item (Phone) Preview")
@Composable
fun ContactItemPhonePreview() {
    ResumeTheme {
        ContactItem(
            contactType = ContactType.PHONE(
                phoneNumber = "1234567890",
                userText = "(123)456-7890",
            )
        )
    }
}

