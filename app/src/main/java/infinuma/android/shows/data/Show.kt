package infinuma.android.shows.data

import androidx.annotation.DrawableRes
import java.io.Serializable

data class Show(
    val Title : String,
    val Description : String,
    @DrawableRes val Image : Int,
    //var reviews : Map <String, Pair <String, String>> = emptyMap()
    ):Serializable