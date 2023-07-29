package infinuma.android.shows.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddReviewRequest(
    @SerialName("comment") val comment: String?,
    @SerialName("rating") val rating: Int,
    @SerialName("show_id") val showID: Int
)