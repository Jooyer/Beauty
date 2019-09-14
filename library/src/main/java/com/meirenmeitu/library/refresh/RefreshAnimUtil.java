package com.meirenmeitu.library.refresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;


/**
 * Created by chqiu on 2016/9/28.
 */

public class RefreshAnimUtil {
    /**
     * 动画执行时间
     */
    public static final long mAnimDuration = 500;

    public static boolean mAnimRunning = false;

    private static final AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mAnimRunning = false;
        }
    };

    private static final ValueAnimator mRefreshAnimator = new ValueAnimator();
    private static final ValueAnimator mFinishRefreshAnimator = new ValueAnimator();
    private static final ValueAnimator mAutoRefreshAnimator = new ValueAnimator();
    private static final ValueAnimator mLoadingAnimator = new ValueAnimator();
    private static final ValueAnimator mFinishLoadAnimator = new ValueAnimator();
    private static final ValueAnimator mNoMoreAnimator = new ValueAnimator();

    public static void startRefreshing(final View view, final View targetView, final TargetHandler handler,
                                       int newHeight, Animator.AnimatorListener listener) {
        mRefreshAnimator.setIntValues(view.getLayoutParams().height, newHeight);
        mRefreshAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (int) animation.getAnimatedValue();
                JRefreshLayout.LayoutParams params = (JRefreshLayout.LayoutParams) view.getLayoutParams();
                params.height = h;
                view.setLayoutParams(params);
                if (targetView != null && handler != null) {
                    handler.handleTarget(targetView, params.height);
                }
            }
        });
        if (listener != null) {
            mRefreshAnimator.addListener(listener);
        } else {
            mRefreshAnimator.addListener(adapter);
        }
        mRefreshAnimator.setDuration(mAnimDuration);
        mAnimRunning = true;
        mRefreshAnimator.start();
    }


    public static void startLoading(final View view, final View targetView, final TargetHandler handler,
                                    int newHeight, Animator.AnimatorListener listener) {
        mLoadingAnimator.setIntValues(view.getLayoutParams().height, newHeight);
        mLoadingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (int) animation.getAnimatedValue();
                JRefreshLayout.LayoutParams params = (JRefreshLayout.LayoutParams) view.getLayoutParams();
                params.height = h;
                view.setLayoutParams(params);
                if (targetView != null && handler != null) {
                    handler.handleTarget(targetView, params.height);
                }
            }
        });
        if (listener != null) {
            mLoadingAnimator.addListener(listener);
        } else {
            mLoadingAnimator.addListener(adapter);
        }
        mLoadingAnimator.setDuration(mAnimDuration);
        mAnimRunning = true;
        mLoadingAnimator.start();
    }

    public static void startAutoRefreshing(final View view, final View targetView, final TargetHandler handler,
                                           int newHeight, Animator.AnimatorListener listener) {
        mAutoRefreshAnimator.setIntValues(view.getLayoutParams().height, newHeight);
        mAutoRefreshAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (int) animation.getAnimatedValue();
                JRefreshLayout.LayoutParams params = (JRefreshLayout.LayoutParams) view.getLayoutParams();
                params.height = h;
                view.setLayoutParams(params);
                if (targetView != null && handler != null) {
                    handler.handleTarget(targetView, params.height);
                }
            }
        });
        if (listener != null) {
            mAutoRefreshAnimator.addListener(listener);
        } else {
            mAutoRefreshAnimator.addListener(adapter);
        }
        mAutoRefreshAnimator.setDuration(mAnimDuration);
        mAnimRunning = true;
        mAutoRefreshAnimator.start();
    }


    public static void startFinishRefresh(final View view, final View targetView, final TargetHandler handler,
                                          int newHeight, Animator.AnimatorListener listener) {
//        System.out.println("startFinishRefresh========= " + view.getLayoutParams().height  + " ======newHeight: " + newHeight);
        mFinishRefreshAnimator.setIntValues(view.getLayoutParams().height, newHeight);
        mFinishRefreshAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (int) animation.getAnimatedValue();
                JRefreshLayout.LayoutParams params = (JRefreshLayout.LayoutParams) view.getLayoutParams();
                params.height = h;
                view.setLayoutParams(params);
                if (targetView != null && handler != null) {
                    handler.handleTarget(targetView, params.height);
                }
            }
        });
        if (listener != null) {
            mFinishRefreshAnimator.addListener(listener);
        } else {
            mFinishRefreshAnimator.addListener(adapter);
        }
        mFinishRefreshAnimator.setStartDelay(500);
        mFinishRefreshAnimator.setDuration(mAnimDuration);
        mAnimRunning = true;
        mFinishRefreshAnimator.start();
    }


    public static void startFinishLoad(final View view, final View targetView, final TargetHandler handler,
                                       int newHeight, Animator.AnimatorListener listener) {
        mFinishLoadAnimator.setIntValues(view.getLayoutParams().height, newHeight);
        mFinishLoadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (int) animation.getAnimatedValue();
                JRefreshLayout.LayoutParams params = (JRefreshLayout.LayoutParams) view.getLayoutParams();
                params.height = h;
                view.setLayoutParams(params);
                if (targetView != null && handler != null) {
                    handler.handleTarget(targetView, params.height);
                }
            }
        });
        if (listener != null) {
            mFinishLoadAnimator.addListener(listener);
        } else {
            mFinishLoadAnimator.addListener(adapter);
        }
        mFinishLoadAnimator.setStartDelay(500);
        mFinishLoadAnimator.setDuration(mAnimDuration);
        mAnimRunning = true;
        mFinishLoadAnimator.start();
    }

    public static void setNoMoreAnimator(final View view, final View targetView, final TargetHandler handler) {
        mNoMoreAnimator.setIntValues(view.getLayoutParams().height, 0);
        mNoMoreAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (int) animation.getAnimatedValue();
                JRefreshLayout.LayoutParams params = (JRefreshLayout.LayoutParams) view.getLayoutParams();
                params.height = h;
                view.setLayoutParams(params);
                if (targetView != null && handler != null) {
                    handler.handleTarget(targetView, params.height);
                }
            }
        });
        mNoMoreAnimator.addListener(adapter);
        mNoMoreAnimator.setDuration(mAnimDuration);
        mAnimRunning = true;
        mNoMoreAnimator.start();
    }

    public static void startScaleAnim(final View view,
                                      float newScale, Animator.AnimatorListener listener) {
        ValueAnimator anim = ValueAnimator.ofFloat(view.getScaleX(), newScale);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float s = Float.parseFloat(animation.getAnimatedValue().toString());
                view.setScaleX(s);
                view.setScaleY(s);
            }
        });
        if (listener != null) {
            anim.addListener(listener);
        }
        anim.setDuration(mAnimDuration);
        anim.start();
    }


}
