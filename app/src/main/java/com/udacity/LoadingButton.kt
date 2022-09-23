package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnRepeat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var rectProgress = 0
    private var arcProgress = 0
    private var backGColor = 0
    private var textColor = 0
    private var buttonText: String? = null

    private var paint = Paint().apply {
        color = resources.getColor(R.color.colorPrimary, null)
        textSize = 55.0f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.NORMAL)
    }

    private var arcValueAnimator = ValueAnimator()
    private var rectValueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        if (new == ButtonState.Loading) {
            startAnimation()
            startAnimation2()
        } else {
            isClickable = true
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthSize = w
        heightSize = h
    }


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0, 0
        ).apply {
            try {
                backGColor = getColor(
                    R.styleable.LoadingButton_backgroundColor,
                    resources.getColor(R.color.colorPrimary, null)
                )
                textColor = getColor(
                    R.styleable.LoadingButton_textColor,
                    resources.getColor(R.color.white, null)
                )
                buttonText = getString(R.styleable.LoadingButton_text)
            } finally {
                recycle()
            }
        }
        isClickable = true
        buttonState = ButtonState.Completed
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (buttonState) {

            ButtonState.Loading -> {
                contentDescription = resources.getString(R.string.loading_button_text)
                paint.color = backGColor
                canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
                paint.color = resources.getColor(R.color.colorPrimaryDark, null)
                canvas.drawRect(rectProgress.toFloat(), 0f, 0f, heightSize.toFloat(), paint)
                paint.color = textColor
                canvas.drawText(
                    context.getString(R.string.loading_button_text),
                    (widthSize / 2).toFloat(),
                    (heightSize / 2).toFloat() + 15,
                    paint
                )
                paint.color = resources.getColor(R.color.colorAccent, null)
                canvas.drawArc(
                    (widthSize.toFloat() * 5 / 8) + 70,
                    (heightSize.toFloat() / 2) - 30,
                    (widthSize.toFloat() * 3 / 4),
                    (heightSize.toFloat() / 2) + 30,
                    0f,
                    arcProgress.toFloat(),
                    true,
                    paint
                )
            }
            else -> {
                contentDescription = context.getString(R.string.download_text)
                paint.color = backGColor
                canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
                paint.color = textColor
                canvas.drawText(
                    buttonText ?: resources.getString(R.string.completed_button_text),
                    (widthSize / 2).toFloat(),
                    (heightSize / 2).toFloat() + 15,
                    paint
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        super.performClick()
        isClickable = false
        buttonState = ButtonState.Loading
        invalidate()
        return true
    }

    private fun startAnimation() {
        arcValueAnimator = ValueAnimator.ofInt(0, 360).setDuration(2000)
        arcValueAnimator.addUpdateListener {
            arcProgress = it.animatedValue as Int
            invalidate()
        }
        arcValueAnimator.start()
    }

    private fun startAnimation2() {
        rectValueAnimator = ValueAnimator.ofInt(0, widthSize).setDuration(2000)
        rectValueAnimator.addUpdateListener {
            rectProgress = it.animatedValue as Int
            invalidate()
        }
        rectValueAnimator.repeatCount = ValueAnimator.INFINITE
        rectValueAnimator.repeatMode = ValueAnimator.RESTART
        rectValueAnimator.doOnRepeat {
            it.end()
            buttonState = ButtonState.Completed
            invalidate()
        }
        rectValueAnimator.start()
    }
}