package infinuma.android.shows.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.RewriteQueriesToDropUnusedColumns

@Entity(tableName = "shows", primaryKeys = ["id"])
data class ShowEntity(
    @ColumnInfo(name = "id") val showId: Int,
    @ColumnInfo(name = "average_rating") val averageRating: Float?,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "no_of_reviews") val numOfReviews: Int,
    @ColumnInfo(name = "title") val title: String
)