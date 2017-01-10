package com.demo.joe.citylist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
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
    private Paint paint;
    private String[] words;

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
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(DensityUtil.dip2px(getContext(),11));
        paint.setFakeBoldText(true);
    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(30,getPaddingTop() + heightSize + getPaddingBottom());
//        } else if (widthMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(30, heightSize);
//        } else if (heightMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(widthSize,getPaddingTop() + heightSize + getPaddingBottom());
//        }
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (words != null && words.length > 0) {
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                Rect bound = new Rect();
                paint.getTextBounds(word, 0, word.length(), bound);
                paint.setColor(Color.parseColor("#E6454A"));
                int x = (cellWidth - bound.width()) / 2;
                int y = i * cellHeight + (cellWidth + bound.width()) / 2;
                canvas.drawText(word, x, y, paint);
            }
        }
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

    private int curIndex = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int y = (int) event.getY();
                int index = y / cellHeight;
                if (words != null && index >= 0 && index < words.length) {
                    if (index != curIndex) {
                        curIndex = index;
                        if (indexChangeListener != null) {
                            indexChangeListener.onIndexChange(words[curIndex]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int y1 = (int) event.getY();
                int index1 = y1 / cellHeight;
                if (words != null && index1 >= 0 && index1 < words.length) {
                    if (index1 != curIndex) {
                        curIndex = index1;
                        if (indexChangeListener != null) {
                            indexChangeListener.onIndexChange(words[curIndex]);
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                curIndex = -1;
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
