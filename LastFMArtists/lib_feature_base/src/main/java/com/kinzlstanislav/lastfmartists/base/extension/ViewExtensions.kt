package com.kinzlstanislav.lastfmartists.base.extension

import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver

fun View.setOnClickListener(touchDown: () -> Unit = {}, touchUp: () -> Unit = {}, cancel: () -> Unit = {}) {
    setOnTouchListener(object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (event != null) {
                when {
                    event.action == MotionEvent.ACTION_DOWN -> touchDown()
                    event.action == MotionEvent.ACTION_CANCEL -> cancel()
                    event.action == MotionEvent.ACTION_UP -> touchUp()
                }
            }
            return true
        }
    })
}

fun View.afterMeasured(f: View.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun View.cancelOngoingAnimation() {
    if (animation != null) {
        animation.cancel()
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.appear() {
    this.animate()
        .alpha(1f)
        .duration = 1500L
}