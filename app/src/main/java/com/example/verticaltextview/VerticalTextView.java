package com.example.verticaltextview;

/*
   Thanks to Mark Allison at Styling Android: Vertical Text - Part 1 for the basis of this project.
   https://blog.stylingandroid.com/verticaltext-part-1/

   This in turn was borrowed from https://stackoverflow.com/questions/1258275/vertical-rotated-label-in-android/7855852#7855852

*/

/*
    This is a reasonable solution for creating a TextView with vertical text. It loses some of
    the capabilities of TextView such as marquees. Other functionality may suffer as well. (This
    is probably due to bypassing super.onDraw() for vertical text. In this implementation, normal
    and upside-down text should have complete functionality although it has not been tested.)
*/

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class VerticalTextView extends AppCompatTextView {
    private final int mTextDirection;

    public VerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalTextView);
        mTextDirection = a.getInt(R.styleable.VerticalTextView_direction, TEXT_ORIENTATION_NORMAL);
        a.recycle();

    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mTextDirection == TEXT_ORIENTATION_BOTTOM_UP ||
                mTextDirection == TEXT_ORIENTATION_TOP_DOWN) {
            super.onMeasure(heightMeasureSpec, widthMeasureSpec);
            setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mTextDirection == TEXT_ORIENTATION_NORMAL) {
            // Just do the normal stuff.
            super.onDraw(canvas);
            return;
        }
        if (mTextDirection == TEXT_ORIENTATION_UPSIDE_DOWN) {
            // Normal stuff on upside-down canvas.
            canvas.save();
            canvas.rotate(180, getWidth() / 2, getHeight() / 2);
            super.onDraw(canvas);
            canvas.restore();
            return;
        }

        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if (mTextDirection == TEXT_ORIENTATION_TOP_DOWN) {
            canvas.translate(getWidth(), 0);
            canvas.rotate(90);
        } else if (mTextDirection == TEXT_ORIENTATION_BOTTOM_UP) {
            canvas.translate(0, getHeight());
            canvas.rotate(-90);
        }

        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

        getLayout().draw(canvas);
        canvas.restore();
    }

    private final int TEXT_ORIENTATION_NORMAL = 0;
    private final int TEXT_ORIENTATION_TOP_DOWN = 1;
    @SuppressWarnings("FieldCanBeLocal")
    private final int TEXT_ORIENTATION_UPSIDE_DOWN = 2;
    private final int TEXT_ORIENTATION_BOTTOM_UP = 3;
}