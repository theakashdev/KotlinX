package com.akashdev.kotlinx

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.akashdev.kotlinx.databinding.XProgressBarBinding

class XProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var typedArray: TypedArray
    private var binding = XProgressBarBinding.inflate(LayoutInflater.from(context), this)

    init {
        attrs?.let {
            typedArray = context.obtainStyledAttributes(it, R.styleable.XProgressBar)

            try {
                //visibility of progress
                binding.mainLayout.isVisible = typedArray.getBoolean(
                    /* index = */ R.styleable.XProgressBar_isVisible,
                    /* defValue = */ true
                )

                //loading text
                binding.textLoading.text = typedArray.getString(
                    /* index = */
                    R.styleable.XProgressBar_loadingText,
                ) /* defValue = */ ?: "Loading..."

                //visibility of loading text
                binding.textLoading.isVisible = typedArray.getBoolean(
                    /* index = */ R.styleable.XProgressBar_isVisibleLoadingText,
                    /* defValue = */ true
                )

                //background color
                binding.mainLayout.setBackgroundColor(
                    typedArray.getColor(
                        /* index = */ R.styleable.XProgressBar_backgroundColor,
                        /* defValue = */ context.getColor(R.color.x_progress_bar_bg_color)
                    )
                )

            } finally {
                typedArray.recycle()
            }
        }
    }

    var isVisible
        get() = typedArray.getBoolean(R.styleable.XProgressBar_isVisible, false)
        set(value) {
            binding.mainLayout.isVisible = value
        }

    var backgroundColor: Int?
        get() = typedArray.getColor(
            R.styleable.XProgressBar_backgroundColor,
            context.getColor(R.color.x_progress_bar_bg_color)
        )
        set(value) {
            binding.mainLayout.setBackgroundColor(value!!)
        }


    var loadingText
        get() = typedArray.getString(R.styleable.XProgressBar_loadingText)
        set(value) {
            binding.textLoading.text = value
        }

    var isVisibleLoadingText
        get() = typedArray.getBoolean(R.styleable.XProgressBar_loadingText, false)
        set(value) {
            binding.textLoading.isVisible = value
        }

}