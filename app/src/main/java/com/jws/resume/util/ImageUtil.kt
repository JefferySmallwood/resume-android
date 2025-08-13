package com.jws.resume.util

import android.content.Context
import coil.request.ImageRequest
import com.jws.resume.R
import kotlinx.coroutines.Dispatchers

fun getDefaultImageRequestBuilder(context: Context, url: String?): ImageRequest.Builder {
    return ImageRequest.Builder(context)
        .dispatcher(Dispatchers.IO)
        .data(data = url)
        .error(R.drawable.profile_error)
        .crossfade(true)
        .crossfade(300)
        .allowHardware(true)
}
