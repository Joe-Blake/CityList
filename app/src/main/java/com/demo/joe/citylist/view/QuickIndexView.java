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
    private float cellHeight;
    private float maxOffset;
    private Paint paint;
    private String[] words;
    private int curIndex = -1;
    private float currentY = -1;
    private int textSize;

    //缩小touch有效范围
    private RectF mStartTouchingArea = new RectF();

    private float mFirstItemBaseLineY;

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

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        cellHeight = fontMetrics.bottom - fontMetrics.top;
        cellWidth = (int)Math.max(cellWidth, paint.measureText(words[0]));
        float areaLeft = (width - cellWidth - getPaddingRight());
        float areaRight = width;
        float areaTop = (height - cellHeight) / 2;
        float areaBottom = areaTop + cellHeight;
        //Log.i("area", areaLeft+" "+areaRight);
        mStartTouchingArea.set(
                areaLeft,
                areaTop,
                areaRight,
                areaBottom);

        Log.i("top", fontMetrics.top + "");
        Log.i("bottom", fontMetrics.bottom + "");
        Log.i("dscent", fontMetrics.descent + "");
        Log.i("ascent", fontMetrics.ascent + "");
        Log.i("lending", fontMetrics.leading + "");
        mFirstItemBaseLineY = (height / 2 - words.length * cellHeight / 2)
                + (cellHeight / 2 - (fontMetrics.descent - fontMetrics.ascent) / 2)
                - fontMetrics.ascent;

        Log.i("frist", mFirstItemBaseLineY + "");
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
                //Log.i("wzj", alphaScale + "");
                paint.setColor(Color.parseColor("#E6454A"));
                paint.setAlpha(alphaScale);
                paint.setTextSize(textSize + textSize * scale);
                //int x = (cellWidth - bound.width()) / 2;
                float drawX = (getWidth() - getPaddingRight() - cellWidth / 2 - maxOffset *
                        scale) + DensityUtil.dip2px(getContext(), 35);
                //float y = i * cellHeight + (cellWidth + bound.width()) / 3 - DensityUtil.dip2px(getContext(), 15);//除数：宽3窄2
                float baseLineY = mFirstItemBaseLineY + cellHeight * i;
                canvas.drawText(word, drawX, baseLineY, paint);
            }
        }
    }

    private int getSelectedIndex(float eventY) {
        currentY = eventY - (getHeight()/2 - cellHeight /2);
        if (currentY <= 0) {
            return 0;
        }

        int index = (int) (currentY / this.cellHeight);
        if (index >= this.words.length) {
            index = this.words.length - 1;
        }
        return index;
    }

    private float getScale(int index) {
        float scale = 0;

        if (curIndex != -1) {
            float distance = Math.abs(currentY - (cellHeight * index + cellHeight /
                    2)) / cellHeight;
            scale = 1 - distance * distance / 16;
            scale = Math.max(scale, 0);
            Log.i("scale", words[index] + ": " + scale);
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

        float y = event.getY();
        float x = event.getX();
        currentY = y - (getHeight() / 2 - cellHeight * words.length / 2);
        int index = (int) (currentY / cellHeight);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (words != null && index >= 0 && index < words.length) {
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
