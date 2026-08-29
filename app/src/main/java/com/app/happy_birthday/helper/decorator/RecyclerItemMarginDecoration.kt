package com.app.happy_birthday.helper.decorator


import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.happy_birthday.helper.Extensions.asDouble
import com.app.happy_birthday.helper.Extensions.printLog
import com.app.happy_birthday.helper.PrefUtils.isEnglishLanguage

enum class Orientation {
    VERTICAL, HORIZONTAL
}

enum class RecyclerLayoutManager {
    LINEAR, GRID
}

data class ConfigDecoration(
    val mRecyclerMargin: Int = 25,
    val mItemMiddleMargin: Int = 12,
    val layoutManager: RecyclerLayoutManager = RecyclerLayoutManager.LINEAR,
    val orientation: Orientation = Orientation.VERTICAL,
    val spanCount: Int = 2,
    val isBottomMarginEnabled: Boolean = true,
    val isTopMarginEnabled: Boolean = true,
)

class RecyclerItemMarginDecoration(
    private val mCd: ConfigDecoration,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val lastPosition = parent.adapter?.itemCount ?: 0
        val position = parent.getChildAdapterPosition(view)
        val pos = position + 1
        "mCd.spanCount:${mCd.spanCount.asDouble()} pos:${pos.asDouble()} mCd.spanCount % pos = ${pos.asDouble() % mCd.spanCount.asDouble()}".printLog(
            "ItemDecoration"
        )
        if (mCd.layoutManager == RecyclerLayoutManager.GRID) {
            when (mCd.orientation) {
                Orientation.VERTICAL -> {
                    if (view.context.isEnglishLanguage()) {
                        if (pos % mCd.spanCount == 1) {
                            outRect.left = mCd.mRecyclerMargin
                            outRect.right = mCd.mItemMiddleMargin / 2
                        } else if (pos % mCd.spanCount == 0) {
                            outRect.right = mCd.mRecyclerMargin
                            outRect.left = mCd.mItemMiddleMargin / 2
                        } else {
                            outRect.left = mCd.mItemMiddleMargin / 2
                            outRect.right = mCd.mItemMiddleMargin / 2
                        }
                    } else {
                        if (pos % mCd.spanCount == 1) {
                            outRect.right = mCd.mRecyclerMargin
                            outRect.left = mCd.mItemMiddleMargin / 2
                        } else if (pos % mCd.spanCount == 0) {
                            outRect.left = mCd.mRecyclerMargin
                            outRect.right = mCd.mItemMiddleMargin / 2
                        } else {
                            outRect.right = mCd.mItemMiddleMargin / 2
                            outRect.left = mCd.mItemMiddleMargin / 2
                        }
                    }
                    if (pos <= (lastPosition - (lastPosition % mCd.spanCount)) && mCd.isBottomMarginEnabled) {
                        outRect.bottom = mCd.mItemMiddleMargin / 2
                    }
                    if (pos > mCd.spanCount && mCd.isTopMarginEnabled){
                        outRect.top = mCd.mItemMiddleMargin / 2
                    }
                }
                Orientation.HORIZONTAL -> {
                    if (view.context.isEnglishLanguage()) {
                        if (pos <= mCd.spanCount) {
                            /** Every first item in a row */
                            outRect.left = mCd.mRecyclerMargin
                            outRect.right = mCd.mItemMiddleMargin
                        } else if (pos <= (lastPosition - (lastPosition % mCd.spanCount))) {
                            /** 2nd item to 2nd last in a row */
                            outRect.right = mCd.mItemMiddleMargin
                        } else {
                            /** Every last item in a row */
                            outRect.right = mCd.mRecyclerMargin
                        }
                    } else {
                        if (pos <= mCd.spanCount) {
                            /** Every first item in a row */
                            outRect.right = mCd.mRecyclerMargin
                            outRect.left = mCd.mItemMiddleMargin
                        } else if (pos <= (lastPosition - (lastPosition % mCd.spanCount))) {
                            /** 2nd item to 2nd last in a row */
                            outRect.left = mCd.mItemMiddleMargin
                        } else {
                            /** Every last item in a row */
                            outRect.left = mCd.mRecyclerMargin
                        }
                    }
                    if (pos % mCd.spanCount != 0) {
                        outRect.bottom = mCd.mItemMiddleMargin
                    }
                }
            }
        } else {
            when (mCd.orientation) {
                Orientation.VERTICAL -> {
                    if (position == 0 && mCd.isTopMarginEnabled) outRect.top = mCd.mRecyclerMargin
                   if (mCd.isBottomMarginEnabled) outRect.bottom = mCd.mItemMiddleMargin
                    outRect.left = mCd.mRecyclerMargin
                    outRect.right = mCd.mRecyclerMargin
                }
                Orientation.HORIZONTAL -> {
                    if (view.context.isEnglishLanguage()) {
                        if (position == 0) {
                            /** First item */
                            outRect.left = mCd.mRecyclerMargin
                            outRect.right = mCd.mItemMiddleMargin / 2
                        } else if (position != lastPosition)
                        /** 2nd to 2nd Last items */
                            outRect.right = mCd.mItemMiddleMargin / 2
                        else
                            outRect.right = mCd.mRecyclerMargin
                        /** Last item */
                    } else {
                        if (position == 0) {
                            /** First item */
                            outRect.left = mCd.mItemMiddleMargin / 2
                            outRect.right = mCd.mRecyclerMargin
                        } else if (position != lastPosition)
                        /** 2nd to 2nd Last items */
                            outRect.left = mCd.mItemMiddleMargin / 2
                        else
                            outRect.left = mCd.mRecyclerMargin
                        /** Last item */
                    }
                }
            }
        }
    }
}