package com.adorastudios.movieappkotlinmvvm.data.remote.retrofit.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ConfigurationResponse (
    @SerialName("images") val images: ImageResponse
)