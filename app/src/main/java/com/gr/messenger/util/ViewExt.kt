package com.gr.messenger.util

import android.view.View

/**
 * Created by gr on 2017/6/12.
 */

fun View.setHeight(height:Int){
    val params=layoutParams
    params.height=height
    layoutParams=params
}