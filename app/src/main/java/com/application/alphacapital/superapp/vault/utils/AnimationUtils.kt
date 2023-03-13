package com.application.alphacapital.superapp.vault.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.View

class AnimationUtils
{
    @SuppressLint("ObjectAnimatorBinding")
    public fun scaleOriginal(view : View) {
        val scaleUpX = ObjectAnimator.ofFloat(
            view, "scaleX", 1f)
        val scaleUpY = ObjectAnimator.ofFloat(
            view, "scaleY", 1f)
        scaleUpX.duration = 200
        scaleUpY.duration = 200
        val scaleUp = AnimatorSet()
        scaleUp.play(scaleUpX).with(scaleUpY)
        scaleUp.start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    public fun scaleDown(view : View) {

        val scaleDownX = ObjectAnimator.ofFloat(view,
            "scaleX", 0.9f)
        val scaleDownY = ObjectAnimator.ofFloat(view,
            "scaleY", 0.9f)
        scaleDownX.duration = 200
        scaleDownY.duration = 200
        val scaleDown = AnimatorSet()
        scaleDown.play(scaleDownX).with(scaleDownY)
        scaleDown.start()
    }
}