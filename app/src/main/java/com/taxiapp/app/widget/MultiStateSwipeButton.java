package com.taxiapp.app.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.taxiapp.vendor.app.R;

/**
 * Created by sarath on 2/12/15.
 */
public class MultiStateSwipeButton extends RelativeLayout implements View.OnTouchListener{

    private Button controlButton;
    private Button sliderButton;

    private RelativeLayout.LayoutParams controlButtonParams;
    private RelativeLayout.LayoutParams sliderButtonParams;

    private float mPreviousX = 0;
    private float mPreviousY = 0;
    private boolean actionDown = false;
    private float swipeDistance = 0;

    private int currentState = 0;
    private String[] controlLabels;
    private int[] controlBackgroud;
    private int[] controlTextColors;
    private int commonStyle;
    private OnChangeStateListener changeStateListener;

    private final int fixedWidth = 700;

    public MultiStateSwipeButton(Context context) {
        super(context);
    }

    public MultiStateSwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    public MultiStateSwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs);
    }

    public void initViews(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.MultiStateSwipeButton, 0, 0);
        try{
            final int controlLabelsId = a.getResourceId(R.styleable.MultiStateSwipeButton_buttonStates, 0);
            controlLabels = getResources().getStringArray(controlLabelsId);

            final int controlColorsId = a.getResourceId(R.styleable.MultiStateSwipeButton_buttonStateColors,0);
            controlBackgroud = getResources().getIntArray(controlColorsId);

            final int controlTextColorsId = a.getResourceId(R.styleable.MultiStateSwipeButton_buttonStateTextColors,0);
            controlTextColors = getResources().getIntArray(controlTextColorsId);



        }finally {
            a.recycle();
        }




        controlButton = new Button(this.getContext());
        controlButtonParams = new RelativeLayout.LayoutParams(fixedWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        controlButton.setLayoutParams(controlButtonParams);
        controlButton.setBackgroundColor(ContextCompat.getColor(this.getContext(), controlBackgroud[0]));
        controlButton.setText(controlLabels[0]);


        sliderButton = new Button(this.getContext());
        sliderButtonParams = new RelativeLayout.LayoutParams(fixedWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        sliderButton.setLayoutParams(sliderButtonParams);
        sliderButtonParams.leftMargin = -fixedWidth;
        sliderButton.setBackgroundColor(ContextCompat.getColor(this.getContext(),controlBackgroud[currentState+1]));
        sliderButton.setText(controlLabels[currentState+1]);

        addView(controlButton);
        addView(sliderButton);
    }

    public void setOnChangeStateListener(OnChangeStateListener listener){
        this.changeStateListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(x < v.getWidth()*3/4){
                    actionDown = true;
                    swipeDistance = x+v.getWidth()*3/4;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(!actionDown){
                    sliderHideStyles();
                    return true;
                }
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                if(sliderButtonParams.leftMargin < 0 ) {

                    if (dx > 0 && (700+sliderButtonParams.leftMargin)*-1 < x ) {
                        sliderButtonParams.setMargins(sliderButtonParams.leftMargin + 6, 0, 0, 0);
                    } else if(dx <0){
                        sliderButtonParams.setMargins(sliderButtonParams.leftMargin - 6, 0, 0, 0);
                    }
                }
                //slideView.setScaleX(0.01f);


                break;
            case MotionEvent.ACTION_UP:
                initSwipeToStart();
                if(mPreviousX >= swipeDistance){
                    moveSliderToEnd();
                    if(changeStateListener != null){
                        changeStateListener.onStateChanged(currentState+1);
                    }
                }else{
                    moveSliderToStart();
                    sliderHideStyles();
                }

                return true;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    public interface OnChangeStateListener{
        public void onStateChanged(int position);
    }

    public void sliderHideStyles(){

        sliderButtonParams.setMargins(-fixedWidth, 0, 0, 0);
    }

    public void moveSliderToEnd(){
        ValueAnimator animator = ValueAnimator.ofInt(sliderButtonParams.leftMargin, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                sliderButtonParams.leftMargin = (Integer) valueAnimator.getAnimatedValue();
                controlButton.requestLayout();
            }
        });
        animator.setDuration(500);
        animator.start();
    }

    public void moveSliderToStart(){
        ValueAnimator animator = ValueAnimator.ofInt(sliderButtonParams.leftMargin,-fixedWidth);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                sliderButtonParams.leftMargin = (Integer) valueAnimator.getAnimatedValue();
                controlButton.requestLayout();
            }
        });
        animator.setDuration(500);
        animator.start();
    }

    public void initSwipeToStart(){
        mPreviousX = 0;
        mPreviousY = 0;
        actionDown = false;
    }
}
