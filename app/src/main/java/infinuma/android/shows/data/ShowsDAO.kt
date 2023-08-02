package infinuma.android.shows.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns

@Dao
interface ShowsDAO {

    @Query("SELECT * FROM shows")
    @RewriteQueriesToDropUnusedColumns
    fun getAllShows() : List<ShowEntity>

    @Query("SELECT * FROM shows WHERE id = :id")
    suspend fun getShow(id: Int) : ShowEntity

    @Insert(entity = ShowEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllShows(shows: List<ShowEntity>)

    @Query("SELECT * FROM reviews WHERE show_id = :showId")
    fun getReviews(showId: Int) : List<ReviewEntity>

    @Insert(entity = ReviewEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReview(reviews: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE id = :reviewId")
    fun getReview(reviewId : String) : ReviewEntity

    @Insert(entity = ReviewEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReviews(reviews: List<ReviewEntity>)
}