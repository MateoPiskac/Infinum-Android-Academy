package infinuma.android.shows.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "image_url") val avatar: String
)