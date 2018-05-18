package com.naruto.mycontacts.Tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.naruto.mycontacts.R;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate ${Date}
 * @Note
 */
public class ImageTextButton extends AppCompatButton {
    private Bitmap image = null;
    private int imageWidth;
    private int imageHeight;
    private String text;
    private float textSize;
    private int textColor;
    private int padding;
    private int paddingTop;
    private int paddingBottom;
    private int paddingHorizontal;
    private int betweenHeight;

    private int textWidth;
    private int textHeight;
    private int viewWidth;
    private int viewHeight;
    private int contentHeight = 0;
    private int contentWidth = 0;
    private Paint paint;
    private int defaultSize = 100;//默认宽高

    public ImageTextButton(Context context) {
        super(context);
    }

    public ImageTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 通过这个方法，将你在atts.xml中定义的declare-styleable的所有属性的值存储到TypedArray中
         */
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImageTextButton);
        //从TypedArray中取出对应的值来为要设置的属性赋值
        int resId = ta.getResourceId(R.styleable.ImageTextButton_image, -1);
        if (resId != -1) {
            Bitmap btm;
            btm = BitmapFactory.decodeResource(getResources(), resId);
            imageHeight = ta.getDimensionPixelSize(R.styleable.ImageTextButton_image_height, 10);
            imageWidth = ta.getDimensionPixelSize(R.styleable.ImageTextButton_image_width, 10);
            if (btm.getWidth() == imageWidth && btm.getHeight() == imageHeight) {
                image = btm;
            } else {
                image = Bitmap.createScaledBitmap(btm, imageWidth, imageHeight, true);
                btm.recycle();
            }
            contentHeight = imageHeight;
            contentWidth = imageWidth;
        }
        text = ta.getString(R.styleable.ImageTextButton_text);
        if (!(text == null || text.equals(""))) {
            textSize = ta.getDimension(R.styleable.ImageTextButton_textSize, 10);
            textColor = ta.getColor(R.styleable.ImageTextButton_textColor, Color.parseColor("#737373"));
            paint = new Paint();
            //paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(textSize);
            paint.setColor(textColor);
            Rect rect = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect);
            textWidth = rect.width();//文本的宽度
            textHeight = rect.height();//文本的高度
            if (textWidth > contentWidth) {
                contentWidth = textWidth;
            }
            contentHeight += textHeight;
        }

        padding = ta.getDimensionPixelSize(R.styleable.ImageTextButton_padding, 0);
        paddingTop = ta.getDimensionPixelSize(R.styleable.ImageTextButton_padding_top, padding);
        paddingHorizontal = ta.getDimensionPixelSize(R.styleable.ImageTextButton_padding_horizontal, padding);
        paddingBottom = ta.getDimensionPixelSize(R.styleable.ImageTextButton_padding_bottom, padding);
        if (padding == 0 && paddingTop != 0 && paddingTop == paddingBottom && paddingBottom == paddingHorizontal) {
            padding = paddingTop;
        }

        betweenHeight = ta.getDimensionPixelSize(R.styleable.ImageTextButton_between_height, 0);
        contentHeight += betweenHeight;
    }

    public ImageTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getSize(widthMeasureSpec);
        viewHeight = getSize(heightMeasureSpec);
        if (viewWidth == -1) {
            viewWidth = contentWidth + paddingHorizontal * 2;
        }
        if (viewHeight == -1) {
            viewHeight = contentHeight + paddingTop + paddingBottom;
        }
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (image != null) {
            canvas.drawBitmap(image, Math.max(getSizeForCenter(imageWidth, viewWidth), paddingHorizontal), getSizeForCenter(contentHeight, viewHeight - paddingTop - paddingBottom) + paddingTop, null);
        }
        if (!(text == null || text.equals(""))) {
            canvas.drawText(text, Math.max(getSizeForCenter(textWidth, viewWidth), paddingHorizontal), getSizeForCenter(contentHeight, viewHeight - paddingTop - paddingBottom) + paddingTop + imageHeight + betweenHeight + textHeight, paint);
        }
    }

    /**
     * 绘制居中图像时获取起始位置
     *
     * @param childSize
     * @param parentSize
     * @return
     */
    private int getSizeForCenter(int childSize, int parentSize) {
        int size = 0;
        if (parentSize > childSize) {
            size = (parentSize - childSize) / 2;
        }
        return size;
    }

    /**
     * 获取尺寸
     *
     * @param measureSpec
     * @return
     */
    private int getSize(int measureSpec) {
        int size = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int measureSize = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                size = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //取-1，标记
                size = -1;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                size = measureSize;
                break;
            }
        }
        return size;
    }


}
