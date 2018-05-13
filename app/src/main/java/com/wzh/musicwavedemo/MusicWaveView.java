package com.wzh.musicwavedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 开发人员: Wzh.
 * 开发日期: 2018/5/13.
 * 开发描述: 音乐波浪的view
 */

public class MusicWaveView extends View {

    private Paint mPaint;

    /**
     * 音乐的个数
     */
    private int mMusicCount;

    /**
     * 矩形的宽度
     */
    private int mRectWidth = 30;

    /**
     * 矩形之间的间距
     */
    private int mOffset = 10;

    /**
     * view的高度
     */
    private int mViewHeight;

    /**
     * 中间矩形的x坐标
     */
    private int mMiddleRectX;

    public MusicWaveView(Context context) {
        this(context, null);
    }

    public MusicWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MusicWaveView);
        mMusicCount = typedArray.getInteger(R.styleable.MusicWaveView_musicCount, 15);
        mMusicCount = mMusicCount <= 5 ? 5 : (mMusicCount >= 30 ? 30 : mMusicCount);
        mOffset = typedArray.getInteger(R.styleable.MusicWaveView_musicOffset, 10);
        mOffset = mOffset <= 10 ? 10 : (mOffset >= 30 ? 30 : mOffset);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result = 300;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            result = Math.min(size, result);
        }
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec)
                , result);
        mViewHeight = getMeasuredHeight();
        mMiddleRectX = getMeasuredWidth() / 2 - mRectWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mMusicCount; i++) {
            float currentHeight = (float) (Math.random() * getMeasuredHeight());
            int j = mMusicCount / 2;
            if (i < j) {//左边
                int lLeft = mMiddleRectX - (mOffset + mRectWidth) * (i + 1);
                if (lLeft >= 0) {
                    canvas.drawRect(lLeft
                            , currentHeight
                            , mMiddleRectX - mOffset * (i + 1) - mRectWidth * i
                            , mViewHeight, mPaint);
                }
            } else if (i == j) {//中间
                canvas.drawRect(mMiddleRectX
                        , currentHeight
                        , mMiddleRectX + mRectWidth
                        , mViewHeight, mPaint);
            } else {//右边
                int k = i - j;
                int rRight = mMiddleRectX + mRectWidth * (k + 1) + mOffset * k;
                if (rRight <= getMeasuredWidth()) {
                    canvas.drawRect(mMiddleRectX + (mRectWidth + mOffset) * k
                            , currentHeight
                            , rRight
                            , mViewHeight, mPaint);
                }
            }
        }

        postInvalidateDelayed(500);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LinearGradient linearGradient = new LinearGradient(0, 0, getMeasuredWidth() * 0.6f / mMusicCount, getMeasuredHeight()
                , new int[]{Color.BLUE, Color.RED, Color.GREEN}, null, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
    }
}
