package com.example.puctime.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

object Utils {

    fun underlineText(context: Context, textView: TextView, color: Int, start: Int) {

        val spannableString = SpannableString(textView.text.toString())
        val lenght = textView.text.toString().length
        val textColor = ContextCompat.getColor(context, color)

        spannableString.setSpan(UnderlineSpan(), start, lenght, 0)
        spannableString.setSpan(ForegroundColorSpan(textColor), start, lenght, 0)
        textView.text = spannableString
    }

    fun changeTextColor(context: Context, textView: TextView, color: Int, start: Int) {

        val spannableString = SpannableString(textView.text.toString())
        val length = textView.text.toString().length
        val textColor = ContextCompat.getColor(context, color)

        spannableString.setSpan(ForegroundColorSpan(textColor), start, length, 0)
        textView.text = spannableString
    }

    fun setLinkClickable(
        activity: Activity,
        linkView: TextView,
        startOfLink: Int,
        desiredActivity: Class<out Activity>
    ) {

        val text = linkView.text
        val length = linkView.text.toString().length


        val spannableString = SpannableString(text)

        val linkClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.context.startActivity(Intent(widget.context, desiredActivity))
                activity.finish()
            }
        }

        spannableString.setSpan(linkClickableSpan, startOfLink, length, 0)

        linkView.text = spannableString
        linkView.isClickable = true
        linkView.movementMethod = LinkMovementMethod.getInstance()

    }
}