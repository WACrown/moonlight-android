package com.limelight.binding.input.advance_setting.superpage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class SuperPagesController {

    public enum BoxPosition{
        Right,
        Left
    }

    private List<SuperPageLayout> pages = new ArrayList<>();
    private BoxPosition boxPosition = BoxPosition.Right;
    private SuperPageLayout.DoubleFingerSwipeListener rightListener;
    private SuperPageLayout.DoubleFingerSwipeListener leftListener;

    private FrameLayout superPagesBox;
    private Context context;

    public SuperPagesController(FrameLayout superPagesBox, Context context) {
        this.superPagesBox = superPagesBox;
        this.context = context;
        rightListener = new SuperPageLayout.DoubleFingerSwipeListener() {
            @Override
            public void onRightSwipe() {
                close();
            }

            @Override
            public void onLeftSwipe() {
                setPosition(BoxPosition.Left);
            }
        };
        leftListener = new SuperPageLayout.DoubleFingerSwipeListener() {
            @Override
            public void onRightSwipe() {
                setPosition(BoxPosition.Right);
            }

            @Override
            public void onLeftSwipe() {
                close();
            }
        };

    }

    public void setPosition(BoxPosition position){

        if (!pages.isEmpty()){
            SuperPageLayout page = pages.get(pages.size() - 1);
            float previousPosition = getVisiblePosition(page);
            boxPosition = position;
            float nextPosition = getVisiblePosition(page);
            ObjectAnimator animator = ObjectAnimator.ofFloat(page, "translationX", previousPosition, nextPosition);
            animator.setDuration(300); // 设置动画持续时间为1秒
            animator.setInterpolator(new AccelerateDecelerateInterpolator()); // 设置动画插值器
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // 动画结束后将视图设置到最终位置
                    page.setX(nextPosition);
                    SuperPageLayout.DoubleFingerSwipeListener doubleFingerSwipeListener = boxPosition == BoxPosition.Right ? rightListener : leftListener;
                    for (SuperPageLayout page : pages){
                        page.setDoubleFingerSwipeListener(doubleFingerSwipeListener);
                    }
                }
            });
            animator.start();
        } else {
            boxPosition = position;
        }
    }

    public boolean isClosed(){
        return pages.isEmpty();
    }

    public void open(SuperPageLayout page){
        pages.add(page);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dpToPx(Integer.parseInt(page.getTag().toString())), ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = dpToPx(20);
        layoutParams.bottomMargin = dpToPx(20);
        superPagesBox.addView(page,layoutParams);
        page.setDoubleFingerSwipeListener(boxPosition == BoxPosition.Right ? rightListener : leftListener);


        float previousPosition = getHidePosition(page);
        float nextPosition = getVisiblePosition(page);
        page.setX(previousPosition);
        ObjectAnimator animator = ObjectAnimator.ofFloat(page, "translationX", previousPosition, nextPosition);
        animator.setDuration(300); // 设置动画持续时间为1秒
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // 设置动画插值器
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束后将视图设置到最终位置
                page.setX(nextPosition);

            }
        });

        //如果新增的page是第一个页面，则不做关闭上一个页面的动作
        if (pages.size() - 2 >= 0){
            SuperPageLayout pagePrevious = pages.get(pages.size() - 2);
            float pagePreviousPreviousPosition = getVisiblePosition(pagePrevious);
            float pagePreviousNextPosition = getHidePosition(pagePrevious);
            ObjectAnimator pagePreviousAnimator = ObjectAnimator.ofFloat(pagePrevious, "translationX", pagePreviousPreviousPosition, pagePreviousNextPosition);
            pagePreviousAnimator.setDuration(300); // 设置动画持续时间为1秒
            pagePreviousAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // 设置动画插值器
            pagePreviousAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // 动画结束后将视图设置到最终位置
                    pagePrevious.setX(pagePreviousNextPosition);
                    animator.start();
                }
            });
            pagePreviousAnimator.start();
        } else {
            animator.start();
        }
    }

    public void close(){
        SuperPageLayout page = pages.get(pages.size() - 1);
        float previousPosition = getVisiblePosition(page);
        float nextPosition = getHidePosition(page);
        ObjectAnimator animator = ObjectAnimator.ofFloat(page, "translationX", previousPosition, nextPosition);
        animator.setDuration(300); // 设置动画持续时间为1秒
        animator.setInterpolator(new AccelerateDecelerateInterpolator());// 设置动画插值器

        ObjectAnimator pagePreviousAnimator = null;
        if (pages.size() - 2 >= 0){
            SuperPageLayout pagePrevious = pages.get(pages.size() - 2);
            float pagePreviousPreviousPosition = getHidePosition(pagePrevious);
            float pagePreviousNextPosition = getVisiblePosition(pagePrevious);
            pagePreviousAnimator = ObjectAnimator.ofFloat(pagePrevious, "translationX", pagePreviousPreviousPosition, pagePreviousNextPosition);
            pagePreviousAnimator.setDuration(300); // 设置动画持续时间为1秒
            pagePreviousAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // 设置动画插值器
            pagePreviousAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // 动画结束后将视图设置到最终位置
                    pagePrevious.setX(pagePreviousNextPosition);
                }
            });
        }
        ObjectAnimator finalPagePreviousAnimator = pagePreviousAnimator;
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束后将视图设置到最终位置
                page.setX(nextPosition);
                superPagesBox.removeView(page);
                pages.remove(page);
                if (finalPagePreviousAnimator != null){
                    finalPagePreviousAnimator.start();
                }

            }
        });
        animator.start();
    }



    private int getHidePosition(SuperPageLayout page){
        if (boxPosition == BoxPosition.Right){
            return superPagesBox.getWidth();
        } else {
            return - dpToPx(Integer.parseInt(page.getTag().toString()));
        }
    }

    private int getVisiblePosition(SuperPageLayout page){
        if (boxPosition == BoxPosition.Right){
            return superPagesBox.getWidth() - dpToPx(20) - dpToPx(Integer.parseInt(page.getTag().toString()));
        } else {
            return dpToPx(20);
        }
    }

    private int dpToPx(int dp){
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
