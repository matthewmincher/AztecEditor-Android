/*
 * Copyright (C) 2016 Automattic
 * Copyright (C) 2015 Matthew Lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wordpress.aztec.spans

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Parcel
import android.text.Layout
import android.text.TextUtils
import org.wordpress.aztec.formatting.BlockFormatter

class AztecUnorderedListSpan : AztecListSpan {

    private val TAG = "ul"

    private var bulletColor: Int = 0
    private var bulletMargin: Int = 0
    private var bulletPadding: Int = 0
    private var bulletWidth: Int = 0


    override var attributes: String = ""
    override var lastItem: AztecListItemSpan = AztecListItemSpan()


    constructor(attributes: String) : super(0) {
        this.attributes = attributes
    }

    constructor(listStyle: BlockFormatter.ListStyle, attributes: String, last: AztecListItemSpan) : super(listStyle.verticalPadding) {
        this.bulletColor = listStyle.indicatorColor
        this.bulletMargin = listStyle.indicatorMargin
        this.bulletWidth = listStyle.indicatorWidth
        this.bulletPadding = listStyle.indicatorPadding
        this.attributes = attributes
        this.lastItem = last
    }

    override fun getStartTag(): String {
        if (TextUtils.isEmpty(attributes)) {
            return TAG
        }
        return TAG + attributes
    }

    override fun getEndTag(): String {
        return TAG
    }



    override fun getLeadingMargin(first: Boolean): Int {
        return bulletMargin + 2 * bulletWidth + bulletPadding
    }

    override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int,
                                   top: Int, baseline: Int, bottom: Int,
                                   text: CharSequence, start: Int, end: Int,
                                   first: Boolean, l: Layout) {
        if (!first) return

        val style = p.style
        val oldColor = p.color

        p.color = bulletColor
        p.style = Paint.Style.FILL

        if (c.isHardwareAccelerated) {
            bulletPath = Path()
            bulletPath!!.addCircle(0.0f, 0.0f + getVerticalPadding(text, end) / 2, bulletWidth.toFloat(), Path.Direction.CW)

            c.save()
            c.translate((x + bulletMargin + dir * bulletWidth).toFloat(), ((top + bottom) / 2.0f))
            c.drawPath(bulletPath!!, p)
            c.restore()
        } else {
            c.drawCircle((x + bulletMargin + dir * bulletWidth).toFloat(), ((top + bottom) / 2.0f) + getVerticalPadding(text, end) / 2, bulletWidth.toFloat(), p)
        }

        p.color = oldColor
        p.style = style

    }

    companion object {
        private var bulletPath: Path? = null
    }
}
