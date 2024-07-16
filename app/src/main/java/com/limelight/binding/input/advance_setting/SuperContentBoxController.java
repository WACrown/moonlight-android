package com.limelight.binding.input.advance_setting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

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


    private List<ViewGroup> pages = new ArrayList<>();

    private int backgroundColor;
    private int backgroundOpacity;

    public SuperContentBoxController(FrameLayout superContentBox, Context context){
        this.superContentBox = (TouchFrameLayout) superContentBox;
        this.context = context;


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

        this.superContentBox.setX(getRightPosition());
        this.superContentBox.setDoubleFingerSwipeListener(rightDoubleFingerSwipeListener);

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

    private int getRightPosition(){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels - dpToPx(270));
    }
    private int getRightHidePosition(){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
    private int getLeftPosition(){
        return (int) dpToPx(20);
    }
    private int getLeftHidePosition(){
        return (int) - dpToPx(250);
    }

    private void changeVisibleStatus(BoxVisibleStatus nextVisibleStatus){

        if (nextVisibleStatus == boxVisibleStatus){
            return;
        }
        if (nextVisibleStatus == BoxVisibleStatus.Visible){
            superContentBox.addView(pages.get(0));
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
                if (nextVisibleStatus == BoxVisibleStatus.Invisible){
                    superContentBox.removeView(pages.get(0));
                    pages.remove(0);
                }
            }
        });
        animator.start();
        this.boxVisibleStatus = nextVisibleStatus;
    }

    private float getPosition(BoxPositionStatus boxPositionStatus, BoxVisibleStatus boxVisibleStatus){
        if (boxPositionStatus == BoxPositionStatus.Right && boxVisibleStatus == BoxVisibleStatus.Visible){
            return getRightPosition();
        } else if (boxPositionStatus == BoxPositionStatus.Right && boxVisibleStatus == BoxVisibleStatus.Invisible) {
            return getRightHidePosition();
        } else if (boxPositionStatus == BoxPositionStatus.Left && boxVisibleStatus == BoxVisibleStatus.Visible) {
            return getLeftPosition();
        } else {
            return getLeftHidePosition();
        }
    }

    private TouchFrameLayout.DoubleFingerSwipeListener getDoubleFingerSwipeListener(BoxPositionStatus boxPositionStatus){
        if (boxPositionStatus == BoxPositionStatus.Right){
            return rightDoubleFingerSwipeListener;
        } else {
            return leftDoubleFingerSwipeListener;
        }
    }


    private void closeLastPage(){
        ViewGroup page = pages.get(pages.size() - 1);
        ViewGroup pagePrevious = pages.get(pages.size() - 2);
        pagePrevious.setVisibility(View.VISIBLE);
        float previousPosition = 0;
        final float nextPosition;
        if (boxPositionStatus == BoxPositionStatus.Right){
            nextPosition = previousPosition + superContentBox.getWidth();
        } else {
            nextPosition = previousPosition - superContentBox.getWidth();
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(page, "translationX", previousPosition, nextPosition);
        animator.setDuration(500); // 设置动画持续时间为1秒
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // 设置动画插值器
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                superContentBox.removeView(page);
                pages.remove(page);
            }
        });
        animator.start();
    }
    private void openNextPage(){
        ViewGroup page = pages.get(pages.size() - 1);
        ViewGroup pagePrevious = pages.get(pages.size() - 2);
        final float previousPosition;
        float nextPosition = 0;
        if (boxPositionStatus == BoxPositionStatus.Right){
            previousPosition = nextPosition + superContentBox.getWidth();
        } else {
            previousPosition = nextPosition - superContentBox.getWidth();
        }
        page.setX(previousPosition);
        superContentBox.addView(page);
        ObjectAnimator animator = ObjectAnimator.ofFloat(page, "translationX", previousPosition, nextPosition);
        animator.setDuration(500); // 设置动画持续时间为0.3秒
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // 设置动画插值器
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束后将视图设置到最终位置
                page.setX(nextPosition);
                pagePrevious.setVisibility(View.GONE);
            }
        });
        animator.start();

    }


    public void setBoxPositionStatus(BoxPositionStatus boxPositionStatus){
        changePositionStatus(boxPositionStatus);
    }


    public void open(ViewGroup viewGroup){
        if (pages.isEmpty()){
            pages.add(viewGroup);
            changeVisibleStatus(BoxVisibleStatus.Visible);
        } else {
            pages.add(viewGroup);
            openNextPage();
        }

    }

    public void close(){
        if (pages.isEmpty()){
            return;
        }
        if (pages.size() == 1){
            changeVisibleStatus(BoxVisibleStatus.Invisible);
        } else {
            closeLastPage();
        }

    }

    public boolean isOpened(){
        return !pages.isEmpty();
    }



}
