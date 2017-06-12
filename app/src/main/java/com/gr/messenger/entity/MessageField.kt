package com.gr.messenger.entity

import android.databinding.ObservableField

/**
 * Created by gr on 2017/6/11.
 */

 class MessageField{
    val name=ObservableField<String>()
    val content=ObservableField<String>()
    val time=ObservableField<String>()
}