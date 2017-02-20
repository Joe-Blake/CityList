package com.demo.joe.citylist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.demo.joe.citylist.utils.DensityUtil;

/**
 * Created by joe on 2016/12/19.
 */
public class QuickIndexView extends View {

//    private final static String[] WORDS = {"#","A","B","C","D","E","F","G","H","I","J",
//            "K","L","M","N","O","P","Q","R","S","T",
//            "U","V","W","X","Y","Z"};

    private int cellWidth;
    private int cellHeight;
    private float maxOffset;
    private Paint paint;
    private String[] words;
    private int curIndex = -1;
    private float currentY = -1;
    private int textSize;

    //缩小touch有效范围
    private RectF mStartTouchingArea = new RectF();

    public void setWords(String[] words) {
        this.words = words;
    }

    public QuickIndexView(Context context) {
        this(context,null);
    }

    public QuickIndexView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QuickIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        maxOffset = DensityUtil.dip2px(getContext(),40);
        textSize = DensityUtil.dip2px(getContext(),12);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.RIGHT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        float areaLeft = (width - cellWidth - getPaddingRight());
        float areaRight = width;
        float areaTop = (height - cellHeight) / 2;
        float areaBottom = areaTop + cellHeight;
        mStartTouchingArea.set(
                areaLeft,
                areaTop,
                areaRight,
                areaBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (words != null && words.length > 0) {
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                Rect bound = new Rect();
                paint.getTextBounds(word, 0, word.length(), bound);

                float scale = getScale(i);
                int alphaScale = (i == curIndex) ? (255) : (int) (255 * (1-scale));
                Log.i("wzj", alphaScale + "");
                paint.setColor(Color.parseColor("#E6454A"));
                paint.setAlpha(alphaScale);
                paint.setTextSize(textSize + textSize * scale);
                //int x = (cellWidth - bound.width()) / 2;
                float drawX = (getWidth() - getPaddingRight() - cellWidth / 2 - maxOffset *
                        scale) + DensityUtil.dip2px(getContext(), 35);
                int y = i * cellHeight + (cellWidth + bound.width()) / 3 - DensityUtil.dip2px(getContext(), 15);//除数：宽3窄2
                canvas.drawText(word, drawX, y, paint);
            }
        }
    }

    private float getScale(int index) {
        float scale = 0;

        if (curIndex != -1) {
            float distance = Math.abs(currentY - (cellHeight * index + cellHeight /
                    2)) / cellHeight;
            scale = 1 - distance * distance / 16;
            scale = Math.max(scale, 0);
            //Log.i("scale", words[index] + ": " + scale);
        }
        return scale;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        cellWidth = getMeasuredWidth();
        if (words != null && words.length > 0) {
            cellHeight = getMeasuredHeight() / words.length;
        } else {
            cellHeight = getMeasuredHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int y = (int) event.getY();
        float x = event.getX();
        int index = y / cellHeight;
        currentY = event.getY() - (getHeight()/2 - cellHeight*words.length /2);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (words != null && index >= 0 && index < words.length && mStartTouchingArea.contains(x, y)) {
                    if (index != curIndex) {
                        curIndex = index;
                        if (indexChangeListener != null) {
                            indexChangeListener.onIndexChange(words[curIndex]);
                        }
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (words != null && index >= 0 && index < words.length) {
                    if (index != curIndex) {
                        curIndex = index;
                        if (indexChangeListener != null) {
                            indexChangeListener.onIndexChange(words[curIndex]);
                        }
                    }
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                curIndex = -1;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                invalidate();
                break;
        }
        return true;
    }

    private OnIndexChangeListener indexChangeListener;

    public void setOnIndexChangeListener(OnIndexChangeListener indexChangeListener) {
        this.indexChangeListener = indexChangeListener;
    }

    public interface OnIndexChangeListener{
        void onIndexChange(String words);
    }
}
