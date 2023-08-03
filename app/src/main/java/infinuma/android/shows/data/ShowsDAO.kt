package infinuma.android.shows.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShowsDAO {

    @Query("SELECT * FROM shows")
    suspend fun getAllShows() : List<ShowEntity>

    @Query("SELECT * FROM shows WHERE showId = :id")
    suspend fun getShow(id: Int) : ShowEntity

    @Insert(entity = ShowEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllShows(shows: List<ShowEntity>)

}