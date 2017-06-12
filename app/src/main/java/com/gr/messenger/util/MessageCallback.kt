package com.gr.messenger.util

import android.graphics.Canvas
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log

/**
 * Created by gr on 2017/6/11.
 */
class MessageCallback : ItemTouchHelper.Callback() {
    private var onItemMove: ((fromPosition: Int, toPosition: Int) -> Boolean)? = null
    private var onItemRemoved: ((position: Int) -> Unit)? = null

    override fun getMovementFlags(p0: RecyclerView?, p1: RecyclerView.ViewHolder?): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(p0: RecyclerView?, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return onItemMove?.invoke(p1.adapterPosition, p2.adapterPosition) ?: false
    }

    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
        onItemRemoved?.invoke(p0.adapterPosition)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG ->
                viewHolder?.itemView?.translationZ = ViewUtil.dp2px(0.5f)
            ItemTouchHelper.ACTION_STATE_SWIPE -> {
                viewHolder?.itemView?.translationZ = ViewUtil.dp2px(-0.36f)
                viewHolder?.itemView?.translationY = ViewUtil.dp2px(0.32f)
            }
        }
    }

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX != 0f) {
            viewHolder?.itemView?.scaleY = 0.979f
        }
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        super.clearView(recyclerView, viewHolder)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder?.itemView?.translationZ = ViewUtil.dp2px(0f)
            viewHolder?.itemView?.translationY = ViewUtil.dp2px(0f)
        }
        viewHolder?.itemView?.scaleY = 1f
    }

    fun setOnItemMoveListener(listener: ((fromPosition: Int, toPosition: Int) -> Boolean)) {
        onItemMove = listener
    }

    fun setOnItemMovedListener(listener: ((position: Int) -> Unit)) {
        onItemRemoved = listener
    }
}