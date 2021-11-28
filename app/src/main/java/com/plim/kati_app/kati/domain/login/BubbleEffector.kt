package com.plim.kati_app.kati.domain.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Path
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.plim.kati_app.R

class BubbleEffector(private val targetView: ViewGroup){

    // Animation Attribute
    private val minMoveTime = 3000
    private val maxMoveTime = 10000
    private val bubbleViewNum = 12

    // View Attribute
    private val minWidthHeight = 400
    private val maxWidthHeight = 700
    private val minAlpha = 1 // 1~10
    private val maxAlpha = 5
    private val drawableId = R.drawable.oval
    private val colorId = R.color.kati_main

    fun start() {
        Log.e("ASDF start", "wait")
        if(targetView.width==0){
            targetView.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            Log.e("ASDF view", "start")
                            for (i in 1..bubbleViewNum) addNewBubble()
                            targetView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    }
            )
        }else{
            Log.e("ASDF view", "start22")
            for (i in 1..bubbleViewNum) addNewBubble()
        }
    }

    fun addNewBubble() {
        val bubbleView = BubbleView(targetView)
        targetView.addView(bubbleView)
        startMove(bubbleView)
    }

    private fun startMove(view: View) {
        val parentView = view.parent as View
        val path = Path().apply {
            setLastPoint(view.x, view.y)
            lineTo(
                    parentView.x + (-view.width..parentView.width).random(),
                    parentView.y + (-view.height..parentView.height).random()
            )
        }
        ObjectAnimator.ofFloat(view, View.X, View.Y, path).apply {
            duration = (minMoveTime..maxMoveTime).random().toLong()
            addListener(AnimationFinishedListener(view))
            start()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    inner class BubbleView(parent: ViewGroup): View(parent.context){
        init {
            background = parent.context.getDrawable(drawableId)
            background.setTint(parent.context.getColor(colorId))
            val wh = (minWidthHeight..maxWidthHeight).random()
            layoutParams = ViewGroup.LayoutParams(wh, wh)
            alpha = ((minAlpha..maxAlpha).random()).toFloat()/10
            elevation = -1F
            x = (-wh..parent.width).random().toFloat()
            y = (-wh..parent.height).random().toFloat()
        }
    }

    inner class AnimationFinishedListener(private val v: View) : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            startMove(v)
        }
    }
}