package infinuma.android.shows.data

import androidx.annotation.DrawableRes

data class Show(
    val Title : String,
    val Description : String,
    @DrawableRes val Image : Int
    ){}