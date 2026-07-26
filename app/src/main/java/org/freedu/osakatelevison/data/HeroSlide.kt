package org.freedu.osakatelevison.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HeroSlide(
    @SerialName("id")
    val id: String,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("image_url")
    val imageUrl: String,

    @SerialName("display_order")
    val displayOrder: Int,

    @SerialName("is_active")
    val isActive: Boolean,

    @SerialName("created_at")
    val createdAt: String? = null
)
