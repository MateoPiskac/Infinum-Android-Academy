package infinuma.android.shows.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class UserEntityConverter {

    @TypeConverter
    fun fromUserEntity(user: UserEntity): String {
        return Gson().toJson(user)
    }

    @TypeConverter
    fun toUserEntity(userJson: String): UserEntity {
        return Gson().fromJson(userJson, UserEntity::class.java)
    }
}
