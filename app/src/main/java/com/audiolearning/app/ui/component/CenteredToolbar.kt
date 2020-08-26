package com.audiolearning.app.ui.component

import android.content.Context
import android.content.res.ColorStateList
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.audiolearning.app.R
import com.google.android.material.appbar.MaterialToolbar

class CenteredToolbar(context: Context, attrs: AttributeSet) : MaterialToolbar(context, attrs) {
    private var tv: TextView? = null
    private val centeredTitleTextView: TextView
        get() {
            if (tv == null) {
                tv = TextView(context)
                tv?.isSingleLine = true
                tv?.ellipsize = TextUtils.TruncateAt.END
                tv?.gravity = Gravity.CENTER
                TextViewCompat.setTextAppearance(
                    tv!!,
                    R.style.TextAppearance_AppCompat_Widget_ActionBar_Title
                )

                val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                lp.gravity = Gravity.CENTER
                tv?.layoutParams = lp

                addView(tv)
            }


            return tv!!
        }

    override fun setTitle(resId: Int) {
        title = resources.getString(resId)
    }

    override fun setTitle(title: CharSequence?) {
        centeredTitleTextView.text = title
    }

    override fun setTitleTextColor(color: Int) {
        centeredTitleTextView.setTextColor(color)
    }

    override fun setTitleTextColor(color: ColorStateList) {
        centeredTitleTextView.setTextColor(color)
    }

    override fun getTitle(): CharSequence = centeredTitleTextView.text.toString()
}
