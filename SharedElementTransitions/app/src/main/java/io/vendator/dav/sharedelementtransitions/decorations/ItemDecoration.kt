package io.vendator.dav.sharedelementtransitions.decorations

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.graphics.Rect
import android.view.View


class ItemDecoration : RecyclerView.ItemDecoration() {

    private val paintBlue: Paint
    private val paintRed: Paint
    private val offset: Int

    init {
        offset = 10
        paintBlue = Paint(Paint.ANTI_ALIAS_FLAG)
        paintBlue.setColor(Color.TRANSPARENT)
        paintBlue.setStyle(Paint.Style.STROKE)
        paintBlue.setStrokeWidth(3F)

        paintRed = Paint(Paint.ANTI_ALIAS_FLAG)
        paintRed.setColor(Color.TRANSPARENT)
        paintRed.setStyle(Paint.Style.STROKE)
        paintRed.setStrokeWidth(1F)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(offset, offset, offset, offset)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val layoutManager = parent.layoutManager

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            c.drawRect(
                    layoutManager.getDecoratedLeft(child).toFloat(),
                    layoutManager.getDecoratedTop(child).toFloat(),
                    layoutManager.getDecoratedRight(child).toFloat(),
                    layoutManager.getDecoratedBottom(child).toFloat(),
                    paintBlue)
            c.drawRect(
                    (layoutManager.getDecoratedLeft(child) + offset).toFloat(),
                    (layoutManager.getDecoratedTop(child) + offset).toFloat(),
                    (layoutManager.getDecoratedRight(child) - offset).toFloat(),
                    (layoutManager.getDecoratedBottom(child) - offset).toFloat(),
                    paintRed)

        }
    }
}