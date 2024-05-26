package com.example.neatrootslearning

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val specModeW = MeasureSpec.getMode(widthMeasureSpec)
        if (specModeW != MeasureSpec.EXACTLY) {
            val layout = layout
            if (layout != null) {
                val width = (layout.getLineRight(0) - layout.getLineLeft(0)).toInt() + compoundPaddingLeft + compoundPaddingRight
                val height = measuredHeight
                setMeasuredDimension(width, height)
            }
        }
    }
}
