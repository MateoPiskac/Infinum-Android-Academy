package infinuma.android.shows.data

import androidx.annotation.DrawableRes
import java.io.Serializable

data class Show(
    val title: String,
    val description: String,
    @DrawableRes val image: Int,
) : Serializable