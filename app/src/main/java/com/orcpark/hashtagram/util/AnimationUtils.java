package com.orcpark.hashtagram.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * Created by JunSeon Park on 14. 3. 14.
 */
public class AnimationUtils {
    public interface OnAnimtaionEndListener {
        public void onAnimationEnd();
    }
    public static void startSoftlyShowAnimation(final View view,
                                                final int duration,
                                                final OnAnimtaionEndListener listener){
        view.animate()
                .alpha(0.5f)
                .scaleX(0.85f)
                .scaleY(0.85f)
                .setDuration(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(1f)
                                .setDuration(duration)
                                .setListener(null);
                        if (listener != null) {
                            listener.onAnimationEnd();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    public static void startSoftlyHideAnimation(final View view, final int duration){
        view.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .alpha(0.5f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
    }

}
