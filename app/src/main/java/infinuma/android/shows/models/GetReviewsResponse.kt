package infinuma.android.shows.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetReviewsResponse (
    @SerialName("reviews")val reviews: List<Review>,
    @SerialName("meta") val meta: Meta
    )

@Serializable
data class Review(
    @SerialName("id") val id: String,
    @SerialName("comment") val comment: String,
    @SerialName("rating") val rating: Int,
    @SerialName("show_id") val showId: Int,
    @SerialName("user") val user: User
)