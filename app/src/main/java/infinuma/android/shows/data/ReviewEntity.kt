package infinuma.android.shows.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "reviews", primaryKeys = ["id"])
data class ReviewEntity(
    @ColumnInfo(name = "id") val reviewId: String,
    @ColumnInfo(name = "comment") val comment: String,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "show_id") val showId: Int,
    @ColumnInfo(name = "user") val user: UserEntity
    )