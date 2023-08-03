package infinuma.android.shows.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReviewsDAO {

    @Query("SELECT * FROM reviews WHERE show_id = :showId")
    fun getReviews(showId: Int): List<ReviewEntity>

    @Insert(entity = ReviewEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReview(reviews: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE reviewId = :reviewId")
    fun getReview(reviewId: String): ReviewEntity

    @Insert(entity = ReviewEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReviews(reviews: List<ReviewEntity>)

}