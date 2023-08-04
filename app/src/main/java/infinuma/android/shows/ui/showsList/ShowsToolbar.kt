package infinuma.android.shows.ui.showsList

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import infinuma.android.shows.R
import infinuma.android.shows.databinding.ShowsToolbarBinding

class ShowsToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {
    private var binding: ShowsToolbarBinding

    init {
        binding = ShowsToolbarBinding.inflate(LayoutInflater.from(context), this)

        val background = GradientDrawable()
        background.shape = GradientDrawable.RECTANGLE
        background.setColor(resources.getColor(R.color.white, null))
        this.background = background
        this.elevation = 4f
    }

    fun setProfileButtonClickListener(listener: OnClickListener) {
        binding.profileButton.setOnClickListener(listener)
    }

    fun setProfilePhoto(photoUri: String?) {
        Glide.with(context)
            .load(photoUri)
            .placeholder(R.drawable.placeholder_profile_picture)
            .into(binding.profileButton)

    }

}