package infinuma.android.shows.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
@Entity(tableName = "shows", primaryKeys = ["id"])
data class Show(
    @SerialName("id") val showId: Int,
    @SerialName("average_rating") val averageRating: Float?,
    @SerialName("description") val description: String,
    @SerialName("image_url") val image: String,
    @SerialName("no_of_reviews") val numOfReviews: Int,
    @SerialName("title") val title: String
) : Serializable