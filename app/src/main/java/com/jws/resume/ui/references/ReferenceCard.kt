package com.jws.resume.ui.references

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jws.resume.R
import com.jws.resume.data.entities.Reference
import com.jws.resume.model.mockResumeData
import com.jws.resume.ui.theme.ResumeTheme
import com.jws.resume.util.ContactResolver
import com.jws.resume.util.ContactType

@Composable
fun ReferenceCard(
    reference: Reference,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(reference.profilePictureUrl)
                        .placeholder(R.drawable.profile_loading)
                        .error(R.drawable.profile_error)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.profile_picture_of, reference.name),
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = reference.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = reference.roleWhenWorked,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            if (reference.description?.isNotEmpty() == true) {
                Text(
                    text = reference.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.worked_together, reference.yearsWorkedTogether),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(16.dp))
                val linkedInContactType = ContactType.Web.LINKEDIN(
                    url = reference.linkedInProfileUrl,
                    userText = stringResource(R.string.view_linkedin)
                )
                TextButton(
                    onClick = {
                        ContactResolver.resolveContact(context, contactType = linkedInContactType)
                    },
                ) {
                    Icon(
                        painterResource(id = R.drawable.inbug_white),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.view_linkedin),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        content
    }
}

@Preview(showBackground = true, name = "Reference Card Preview")
@Composable
fun ReferenceCardPreview() {
    ResumeTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
        ) {
            ReferenceCard(reference = mockResumeData.references[0])
        }
    }
}