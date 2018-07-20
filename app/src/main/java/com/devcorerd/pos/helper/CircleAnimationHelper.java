package com.devcorerd.pos.helper;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Ing. Wilson Garcia
 * Created on 7/19/18
 */
public class CircleAnimationHelper {
    private static final int DEFAULT_DURATION = 500;
    private static final int DEFAULT_DURATION_DISAPPEAR = 200;
    private View mTarget;
    private View mDest;

    private float originX;
    private float originY;
    private float destX;
    private float destY;

    private int mCircleDuration = DEFAULT_DURATION;
    private int mMoveDuration = DEFAULT_DURATION;
    private int mDisappearDuration = DEFAULT_DURATION_DISAPPEAR;

    private WeakReference<Activity> mContextReference;

    private Bitmap mBitmap;
    private CircleImageView mImageView;
    private Animator.AnimatorListener mAnimationListener;

    public CircleAnimationHelper() {
    }

    public CircleAnimationHelper attachActivity(Activity activity) {
        mContextReference = new WeakReference<>(activity);
        return this;
    }

    public CircleAnimationHelper setTargetView(View view) {
        mTarget = view;
        setOriginRect(mTarget.getWidth(), mTarget.getHeight());
        return this;
    }

    private CircleAnimationHelper setOriginRect(float x, float y) {
        originX = x;
        originY = y;
        return this;
    }

    private CircleAnimationHelper setDestRect(float x, float y) {
        destX = x;
        destY = y;
        return this;
    }

    public CircleAnimationHelper setDestView(View view) {
        mDest = view;
        setDestRect(mDest.getWidth(), mDest.getWidth());
        return this;
    }

    public CircleAnimationHelper setCircleDuration(int duration) {
        mCircleDuration = duration;
        return this;
    }

    public CircleAnimationHelper setMoveDuration(int duration) {
        mMoveDuration = duration;
        return this;
    }

    private boolean prepare() {
        if (mContextReference.get() != null) {
            ViewGroup decoreView = (ViewGroup) mContextReference.get().getWindow().getDecorView();

            mBitmap = drawViewToBitmap(mTarget, mTarget.getWidth(), mTarget.getHeight());
            if (mImageView == null)
                mImageView = new CircleImageView(mContextReference.get());
            mImageView.setImageBitmap(mBitmap);
            mImageView.setBorderWidth(mImageView.getBorderWidth());
            mImageView.setBorderColor(mImageView.getBorderColor());

            int[] src = new int[2];
            mTarget.getLocationOnScreen(src);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mTarget.getWidth(), mTarget.getHeight());
            params.setMargins(src[0], src[1], 0, 0);
            if (mImageView.getParent() == null)
                decoreView.addView(mImageView, params);
        }
        return true;
    }

    public void startAnimation() {

        if (prepare()) {
            getAvatarRevealAnimator().start();
        }
    }

    private AnimatorSet getAvatarRevealAnimator() {
        final float endRadius = Math.max(destX, destY) / 2;
        final float startRadius = Math.max(originX, originY);


        final float scaleFactor = 0.5f;
        Animator scaleAnimatorY = ObjectAnimator.ofFloat(mImageView, View.SCALE_Y, 1, 1, scaleFactor, scaleFactor);
        Animator scaleAnimatorX = ObjectAnimator.ofFloat(mImageView, View.SCALE_X, 1, 1, scaleFactor, scaleFactor);

        AnimatorSet animatorCircleSet = new AnimatorSet();
        animatorCircleSet.setDuration(mCircleDuration);
        animatorCircleSet.playTogether(scaleAnimatorX, scaleAnimatorY);
        animatorCircleSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mAnimationListener != null)
                    mAnimationListener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int[] src = new int[2];
                int[] dest = new int[2];
                mImageView.getLocationOnScreen(src);
                mDest.getLocationOnScreen(dest);

                float y = mImageView.getY();
                float x = mImageView.getX();
                Animator translatorX = ObjectAnimator.ofFloat(mImageView, View.X, x, x + dest[0] - (src[0] + (originX * scaleFactor - 2 * endRadius * scaleFactor) / 2) + (0.5f * destX - scaleFactor * endRadius));
                translatorX.setInterpolator(new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float input) {
//                        return (float) (Math.sin((0.5f * input) * Math.PI));
                        //-(1-x)^2+1
                        return (float) (-Math.pow(input - 1, 2) + 1f);
                    }
                });
                Animator translatorY = ObjectAnimator.ofFloat(mImageView, View.Y, y, y + dest[1] - (src[1] + (originY * scaleFactor - 2 * endRadius * scaleFactor) / 2) + (0.5f * destY - scaleFactor * endRadius));
                translatorY.setInterpolator(new LinearInterpolator());

                AnimatorSet animatorMoveSet = new AnimatorSet();
                animatorMoveSet.playTogether(translatorX, translatorY);
                animatorMoveSet.setDuration(mMoveDuration);

                AnimatorSet animatorDisappearSet = new AnimatorSet();
                Animator disappearAnimatorY = ObjectAnimator.ofFloat(mImageView, View.SCALE_Y, scaleFactor, 0);
                Animator disappearAnimatorX = ObjectAnimator.ofFloat(mImageView, View.SCALE_X, scaleFactor, 0);
                animatorDisappearSet.setDuration(mDisappearDuration);
                animatorDisappearSet.playTogether(disappearAnimatorX, disappearAnimatorY);


                AnimatorSet total = new AnimatorSet();
                total.playSequentially(animatorMoveSet, animatorDisappearSet);
                total.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mAnimationListener != null)
                            mAnimationListener.onAnimationEnd(animation);
                        reset();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                total.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return animatorCircleSet;
    }

    private Bitmap drawViewToBitmap(View view, int width, int height) {
        Drawable drawable = new BitmapDrawable();
//        view.layout(0, 0, width, height);
        Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(dest);
        drawable.setBounds(new Rect(0, 0, width, height));
        drawable.draw(c);
        view.draw(c);
//        view.layout(0, 0, width, height);
        return dest;
    }

    private void reset() {
        mBitmap.recycle();
        mBitmap = null;
        if (mImageView.getParent() != null)
            ((ViewGroup) mImageView.getParent()).removeView(mImageView);
        mImageView = null;
        //mTarget.setVisibility(View.VISIBLE);
    }

    public CircleAnimationHelper setAnimationListener(Animator.AnimatorListener listener) {
        mAnimationListener = listener;
        return this;
    }
}
