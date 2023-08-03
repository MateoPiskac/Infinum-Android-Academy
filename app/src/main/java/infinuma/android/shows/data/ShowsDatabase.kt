package infinuma.android.shows.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ShowEntity::class, ReviewEntity::class, UserEntity::class],
    version = 1
)
//@TypeConverters(UserEntityConverter::class)
abstract class ShowsDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: ShowsDatabase? = null

        fun getDatabase(context: Context): ShowsDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context = context,
                    klass = ShowsDatabase::class.java,
                    name = "shows_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = database
                database
            }
        }

    }

    abstract fun showDAO(): ShowsDAO
    abstract fun reviewDAO(): ReviewsDAO
    abstract fun usersDAO(): UsersDAO
}