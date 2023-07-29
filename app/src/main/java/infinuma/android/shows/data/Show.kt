package infinuma.android.shows.data

import java.io.Serializable
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Show(
    @SerialName("id")val showId: Int,
    @SerialName("average_rating")val averageRating: Float?,
    @SerialName("description")val description: String,
    @SerialName("image_url") val image: String = "app/src/main/res/drawable/ic_office.png",
    @SerialName("no_of_reviews")val numOfReviews: Int,
    @SerialName("title")val title: String,
) : Serializable