package com.example.jellyfintv.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.example.jellyfintv.R
import com.example.jellyfintv.ui.model.MediaItem

class CardPresenter : Presenter() {
    private var mDefaultCardImage: Int = 0
    private var mSelectedBackgroundColor: Int = 0
    private var mDefaultBackgroundColor: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val context = parent.context
        
        mDefaultCardImage = context.getDrawable(R.drawable.default_background)?.hashCode() ?: 0
        
        val cardView = ImageCardView(context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setMainImageAdjustViewBounds(true)
            setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        }
        
        // Set background colors
        mDefaultBackgroundColor = context.getColor(R.color.lb_basic_card_bg_color)
        mSelectedBackgroundColor = context.getColor(R.color.lb_basic_card_bg_color_pressed)
        
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val mediaItem = item as MediaItem
        val cardView = viewHolder.view as ImageCardView
        
        cardView.titleText = mediaItem.title
        cardView.contentText = mediaItem.subtitle
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        
        // Load image with Glide
        val context = cardView.context
        Glide.with(context)
            .load(mediaItem.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.default_background)
            .error(R.drawable.default_background)
            .into(cardView.mainImageView)
        
        // Set background color
        cardView.setBackgroundColor(mDefaultBackgroundColor)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        
        // Reset the image
        cardView.mainImage = null
        cardView.badgeImage = null
    }

    companion object {
        private const val CARD_WIDTH = 313
        private const val CARD_HEIGHT = 176
    }
}
