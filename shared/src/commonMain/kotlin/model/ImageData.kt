package model

import kotlinx.serialization.Serializable

@Serializable
data class ImageData(
    val author: String,
    val category: String,
    val path: String
)
