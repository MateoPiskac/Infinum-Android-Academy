package infinuma.android.shows.models

import infinuma.android.shows.data.Show
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetShowsResponse(
    @SerialName("shows") val shows: List<Show>,
    @SerialName("meta") val meta: Meta
)

@Serializable
data class Meta(
    @SerialName("pagination")val pagination: Pagination
)

@Serializable
data class Pagination(
    val count: Int,
    val page: Int,
    val items: Int,
    val pages: Int
)