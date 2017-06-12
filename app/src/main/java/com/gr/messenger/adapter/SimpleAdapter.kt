package com.gr.messenger.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by gr on 2017/6/11.
 */

abstract class SimpleAdapter<VH : SimpleAdapter.CommonViewHolder, E>(
        protected val context: Context,
        protected val datas: ArrayList<E>,
        private val layoutId: Int
) : RecyclerView.Adapter<VH>() {
    protected var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        @Suppress("UNCHECKED_CAST")
        return CommonViewHolder(DataBindingUtil.inflate(inflater, layoutId, parent, false)) as VH
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindTo(holder, position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    abstract fun bindTo(holder: VH, position: Int)

    open class CommonViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}