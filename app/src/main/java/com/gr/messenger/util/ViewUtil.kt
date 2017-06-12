package com.gr.messenger.util

import android.util.TypedValue

/**
 * Created by gr on 2017/6/11.
 */
object ViewUtil {
    private val context=AppUtil.application

    fun dp2px(dp:Float):Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, context.resources.displayMetrics)
    }
}