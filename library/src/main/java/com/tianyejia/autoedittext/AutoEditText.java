package com.tianyejia.autoedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;

public class AutoEditText extends AppCompatEditText {
    private int textSize;
    private String leftText;
    private int leftTextColor;
    private float leftTextSize;
    private int leftTextPadding;
    private boolean isShowBottomLine;
    private int bottomLineDefaultColor;
    private int bottomLineFocusColor;

    private Paint mLeftTextPaint;
    private Paint mLinePaint;
    private int colorAccent;

    private float defaultBottomLineWidth = 0.5f;
    private float focusBottomLineWidth = 1f;

    public AutoEditText(Context context) {
        this(context, null);
    }

    public AutoEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public AutoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.AutoEditText, defStyleAttr, 0);
        TypedArray defaultTypedArray = context.obtainStyledAttributes(attrs, new int[]{R.attr.colorAccent, android.R.attr.textSize});

        colorAccent = defaultTypedArray.getColor(0, Color.BLACK);
        textSize = defaultTypedArray.getDimensionPixelSize(1, getResources().getDimensionPixelSize(R.dimen.text_left_size));

        leftText = typeArray.getString(R.styleable.AutoEditText_leftText);
        leftTextColor = typeArray.getColor(R.styleable.AutoEditText_leftTextColor, Color.BLACK);
        leftTextSize = typeArray.getDimensionPixelSize(R.styleable.AutoEditText_leftTextSize, textSize);
        leftTextPadding = typeArray.getDimensionPixelSize(R.styleable.AutoEditText_leftTextPadding, getResources().getDimensionPixelSize(R.dimen.text_left_padding));


        isShowBottomLine = typeArray.getBoolean(R.styleable.AutoEditText_isShowBottomLine, true);
        bottomLineDefaultColor = typeArray.getColor(R.styleable.AutoEditText_bottomLineDefaultColor, Color.BLACK);
        bottomLineFocusColor = typeArray.getColor(R.styleable.AutoEditText_bottomLineFocusColor, colorAccent);

        typeArray.recycle();
        defaultTypedArray.recycle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }

        mLeftTextPaint = new Paint();
        mLeftTextPaint.setColor(leftTextColor);
        mLeftTextPaint.setTextSize(leftTextSize);
        mLeftTextPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setColor(bottomLineDefaultColor);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(dip2px(defaultBottomLineWidth));

        if (!TextUtils.isEmpty(leftText)) {
            int paddingLeft = (int) mLeftTextPaint.measureText(leftText) + getPaddingLeft() + leftTextPadding;
            setPadding(paddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (isShowBottomLine) {

            if (focused) {
                mLinePaint.setStrokeWidth(dip2px(focusBottomLineWidth));
                mLinePaint.setColor(bottomLineFocusColor);
            } else {
                mLinePaint.setStrokeWidth(dip2px(defaultBottomLineWidth));
                mLinePaint.setColor(bottomLineDefaultColor);
            }
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(leftText)) {
            canvas.drawText(leftText, getScrollX(), (getMeasuredHeight() - mLeftTextPaint.getTextSize()) / 2 + mLeftTextPaint.getTextSize(), mLeftTextPaint);
        }

        if (isShowBottomLine) {
            canvas.drawLine(getScrollX(), this.getHeight() - dip2px(1),
                    getScrollX() + this.getWidth(), this.getHeight() - dip2px(1), mLinePaint);
        }
    }

    //dp和px的转换关系比例值
    public int dip2px(double dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5);
    }

    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
