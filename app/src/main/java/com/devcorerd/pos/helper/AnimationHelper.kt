package com.devcorerd.pos.helper

import android.view.View
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

/**
 * @author Ing. Wilson Garcia
 * Created on 7/21/18
 */
class AnimationHelper private constructor() {
    companion object {
        fun animateView(viewToAnimate: View, animation: Techniques,
                        onStartCallback: (()-> Unit)? = null,
                        onEndCallback: (()-> Unit)? = null) {
            YoYo.with(animation)
                    .duration(200)
                    .repeat(0)
                    .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                    .onStart {
                        onStartCallback?.invoke()
                    }.onEnd {
                        onEndCallback?.invoke()
                    }
                    .playOn(viewToAnimate)
        }
    }
}