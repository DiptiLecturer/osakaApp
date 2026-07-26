package org.freedu.osakatelevison.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product (
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("category")
    val category: String,

    @SerialName("size")
    val size: String,

    @SerialName("price")
    val price: Double,

    @SerialName("description")
    val description: String? = null,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("is_active")
    val isActive: Boolean,

    @SerialName("original_price")
    val originalPrice: Double? = null,

    @SerialName("discount_percentage")
    val discountPercentage: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null
)