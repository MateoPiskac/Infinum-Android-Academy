package infinuma.android.shows.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "users", primaryKeys = ["id"])
data class UserEntity(
    @ColumnInfo(name = "user_id")val id: Int,
    @ColumnInfo(name = "email")val email: String,
    @ColumnInfo(name = "profile_image")val avatar: String
)