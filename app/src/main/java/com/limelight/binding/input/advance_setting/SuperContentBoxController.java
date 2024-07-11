package com.limelight.binding.input.advance_setting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

public class SuperContentBoxController {


    private enum BoxVisibleStatus {
        Invisible,
        Visible
    }

    public enum BoxPositionStatus {
        Right,
        Left
    }

    private TouchFrameLayout superContentBox;
    private BoxVisibleStatus boxVisibleStatus = BoxVisibleStatus.Invisible;
    private BoxPositionStatus boxPositionStatus = BoxPositionStatus.Right;
    private Context context;

    private TouchFrameLayout.DoubleFingerSwipeListener rightDoubleFingerSwipeListener;
    private TouchFrameLayout.DoubleFingerSwipeListener leftDoubleFingerSwipeListener;

    private float rightPosition;
    private float rightHidePosition;
    private float leftPosition;
    private float leftHidePosition;

    private int backgroundColor;
    private int backgroundOpacity;

    public SuperContentBoxController(FrameLayout superContentBox, Context context){
        this.superContentBox = (TouchFrameLayout) superContentBox;
        this.context = context;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        rightPosition = displayMetrics.heightPixels - dpToPx(270);
        rightHidePosition = displayMetrics.heightPixels;
        leftPosition = dpToPx(20);
        leftHidePosition = - dpToPx(250);


        rightDoubleFingerSwipeListener = new TouchFrameLayout.DoubleFingerSwipeListener() {
            @Override
            public void onRightSwipe() {
                close();
            }

            @Override
            public void onLeftSwipe() {
                changePositionStatus(BoxPositionStatus.Left);
            }
        };

        leftDoubleFingerSwipeListener = new TouchFrameLayout.DoubleFingerSwipeListener() {
            @Override
            public void onRightSwipe() {
                changePositionStatus(BoxPositionStatus.Right);
            }

            @Override
            public void onLeftSwipe() {
                close();
            }
        };

        this.superContentBox.setX(rightHidePosition);
        this.superContentBox.setDoubleFingerSwipeListener(rightDoubleFingerSwipeListener);

    }


    public void open(ViewGroup viewGroup, BoxPositionStatus boxPositionStatus){
        changePositionStatus(boxPositionStatus);
        changeVisibleStatus(BoxVisibleStatus.Visible);
    }

    public void open(ViewGroup viewGroup){
        changeVisibleStatus(BoxVisibleStatus.Visible);
    }

    public void close(){
        changeVisibleStatus(BoxVisibleStatus.Invisible);
    }

    private float dpToPx(float dp){
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private void changePositionStatus(BoxPositionStatus nextPositionStatus){
        if (nextPositionStatus == boxPositionStatus){
            return;
        }

        if (boxVisibleStatus == BoxVisibleStatus.Invisible){
            superContentBox.setX(getPosition(nextPositionStatus,boxVisibleStatus));
        } else {
            float previousPosition = getPosition(boxPositionStatus,boxVisibleStatus);
            float nextPosition = getPosition(nextPositionStatus,boxVisibleStatus);
            ObjectAnimator animator = ObjectAnimator.ofFloat(superContentBox, "translationX", previousPosition, nextPosition);
            animator.setDuration(500); // 设置动画持续时间为1秒
            animator.setInterpolator(new AccelerateDecelerateInterpolator()); // 设置动画插值器
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // 动画结束后将视图设置到最终位置
                    superContentBox.setX(nextPosition);
                }
            });
            animator.start();
        }
        boxPositionStatus = nextPositionStatus;
        superContentBox.setDoubleFingerSwipeListener(getDoubleFingerSwipeListener(nextPositionStatus));

    }

    private void changeVisibleStatus(BoxVisibleStatus nextVisibleStatus){

        if (nextVisibleStatus == boxVisibleStatus){
            return;
        }
        float previousPosition = getPosition(boxPositionStatus,boxVisibleStatus);
        float nextPosition = getPosition(boxPositionStatus,nextVisibleStatus);
        ObjectAnimator animator = ObjectAnimator.ofFloat(superContentBox, "translationX", previousPosition, nextPosition);
        animator.setDuration(500); // 设置动画持续时间为1秒
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // 设置动画插值器
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束后将视图设置到最终位置
                superContentBox.setX(nextPosition);
            }
        });
        animator.start();
        this.boxVisibleStatus = nextVisibleStatus;
    }

    private float getPosition(BoxPositionStatus boxPositionStatus, BoxVisibleStatus boxVisibleStatus){
        if (boxPositionStatus == BoxPositionStatus.Right && boxVisibleStatus == BoxVisibleStatus.Visible){
            return rightPosition;
        } else if (boxPositionStatus == BoxPositionStatus.Right && boxVisibleStatus == BoxVisibleStatus.Invisible) {
            return rightHidePosition;
        } else if (boxPositionStatus == BoxPositionStatus.Left && boxVisibleStatus == BoxVisibleStatus.Visible) {
            return leftPosition;
        } else {
            return leftHidePosition;
        }
    }

    private TouchFrameLayout.DoubleFingerSwipeListener getDoubleFingerSwipeListener(BoxPositionStatus boxPositionStatus){
        if (boxPositionStatus == BoxPositionStatus.Right){
            return rightDoubleFingerSwipeListener;
        } else {
            return leftDoubleFingerSwipeListener;
        }
    }

}
