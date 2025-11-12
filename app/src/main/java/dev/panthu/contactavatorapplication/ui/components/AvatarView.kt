package dev.panthu.contactavatorapplication.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import coil.load
import coil.transform.CircleCropTransformation
import dev.panthu.contactavatorapplication.R

/**
 * Custom circular avatar view with support for both resource IDs and custom URIs.
 * Features:
 * - Automatic circular cropping
 * - Efficient image loading with Coil
 * - Custom URI support with fallback handling
 * - Optional border styling
 * - Graceful error handling with default avatar
 * - Accessibility support with content descriptions
 */
class AvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var borderPaint: Paint? = null
    private var showBorder: Boolean = false
    private var borderColor: Int = 0
    private var borderWidth: Float = 0f

    @DrawableRes
    private var placeholderDrawable: Int = R.drawable.avatar_default

    @DrawableRes
    private var errorDrawable: Int = R.drawable.avatar_default

    init {
        // Parse custom attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AvatarView,
            defStyleAttr,
            0
        ).apply {
            try {
                val avatarResId = getResourceId(R.styleable.AvatarView_avatarResourceId, 0)
                val avatarUriString = getString(R.styleable.AvatarView_avatarUri)
                showBorder = getBoolean(R.styleable.AvatarView_showBorder, false)
                borderColor = getColor(
                    R.styleable.AvatarView_borderColor,
                    context.getColor(R.color.primary)
                )
                borderWidth = getDimension(
                    R.styleable.AvatarView_borderWidth,
                    context.resources.getDimension(R.dimen.avatar_border_width)
                )
                placeholderDrawable = getResourceId(
                    R.styleable.AvatarView_placeholderDrawable,
                    R.drawable.avatar_default
                )
                errorDrawable = getResourceId(
                    R.styleable.AvatarView_errorDrawable,
                    R.drawable.avatar_default
                )

                // Set initial avatar
                when {
                    avatarUriString != null -> setAvatarUri(avatarUriString)
                    avatarResId != 0 -> setAvatarResource(avatarResId)
                    else -> setAvatarResource(R.drawable.avatar_default)
                }
            } finally {
                recycle()
            }
        }

        // Initialize border paint if needed
        if (showBorder) {
            initBorderPaint()
        }

        // Set default content description
        if (contentDescription.isNullOrEmpty()) {
            contentDescription = context.getString(R.string.avatar_content_description)
        }
    }

    private fun initBorderPaint() {
        borderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            color = borderColor
            isAntiAlias = true
        }
    }

    /**
     * Set avatar from a resource ID with circular cropping.
     */
    fun setAvatarResource(@DrawableRes resId: Int) {
        load(resId) {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(placeholderDrawable)
            error(errorDrawable)
        }
    }

    /**
     * Set avatar from a URI string with fallback to default on error.
     */
    fun setAvatarUri(uriString: String?) {
        if (uriString.isNullOrEmpty()) {
            setAvatarResource(R.drawable.avatar_default)
            return
        }

        try {
            val uri = Uri.parse(uriString)
            load(uri) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(placeholderDrawable)
                error(errorDrawable)
                listener(
                    onError = { _, _ ->
                        // Fallback to default avatar on error
                        post { setAvatarResource(R.drawable.avatar_default) }
                    }
                )
            }
        } catch (e: Exception) {
            // Invalid URI format, use default
            setAvatarResource(R.drawable.avatar_default)
        }
    }

    /**
     * Set avatar from a Uri object.
     */
    fun setAvatarUri(uri: Uri?) {
        if (uri == null) {
            setAvatarResource(R.drawable.avatar_default)
            return
        }

        load(uri) {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(placeholderDrawable)
            error(errorDrawable)
            listener(
                onError = { _, _ ->
                    // Fallback to default avatar on error
                    post { setAvatarResource(R.drawable.avatar_default) }
                }
            )
        }
    }

    /**
     * Enable or disable border display.
     */
    fun setShowBorder(show: Boolean) {
        showBorder = show
        if (show && borderPaint == null) {
            initBorderPaint()
        }
        invalidate()
    }

    /**
     * Set border color.
     */
    fun setBorderColor(color: Int) {
        borderColor = color
        borderPaint?.color = color
        invalidate()
    }

    /**
     * Set border width in pixels.
     */
    fun setBorderWidth(width: Float) {
        borderWidth = width
        borderPaint?.strokeWidth = width
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw border if enabled
        if (showBorder && borderPaint != null) {
            val centerX = width / 2f
            val centerY = height / 2f
            val radius = (Math.min(width, height) / 2f) - (borderWidth / 2f)
            canvas.drawCircle(centerX, centerY, radius, borderPaint!!)
        }
    }
}
