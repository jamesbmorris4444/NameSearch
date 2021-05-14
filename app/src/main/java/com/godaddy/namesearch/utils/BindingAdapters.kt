package com.godaddy.namesearch.utils

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel

@BindingAdapter("background_from_res_int")
fun setBackgroundr(view: EditText, resInt: Int) {
    view.setBackgroundResource(resInt)
    view.requestLayout()
}

@BindingAdapter("image_background")
fun setBackgroundImage(imageView: ImageView, resource: Drawable) {
    imageView.setImageDrawable(resource)
}

@BindingAdapter("layout_marginTop")
fun setMarginTop(v: View, topMargin: Int) {
    v.layoutParams ?: return
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.topMargin = topMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("layout_marginLeft")
fun setMarginLeft(v: View, leftMargin: Int) {
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.leftMargin = leftMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("layout_marginRight")
fun setMarginRight(v: View, rightMargin: Int) {
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.rightMargin = rightMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("textview_gravity")
fun setTextViewGravity(v: TextView, gravity: Int) {
    v.gravity = gravity
    v.requestLayout()
}

@BindingAdapter("text_color")
fun setTextColor(view: TextView, color: String) {
    view.setTextColor(Color.parseColor(color))
    // If color is ever needed as a drawable: Converters.convertColorToDrawable(0x00ff00)
}

@BindingAdapter("edit_text_color")
fun setEditTextColor(view: EditText, color: String) {
    view.setTextColor(Color.parseColor(color))
    // If color is ever needed as a drawable: Converters.convertColorToDrawable(0x00ff00)
}

@BindingAdapter("background_color")
fun setBackgroundColor(view: LinearLayout, color: String) {
    view.setBackgroundColor(Color.parseColor(color))
}

@BindingAdapter("text_size")
fun setTextSize(view: TextView, size: Float) {
    view.textSize = size
}

@BindingAdapter("edit_text_size")
fun setEditTextSize(view: EditText, size: Float) {
    view.textSize = size
}

@BindingAdapter("layout_width_dynamic")
fun setLayoutWidth(v: View, width: Int) {
    v.layoutParams.width = width
    v.requestLayout()
}

@BindingAdapter("layout_height_dynamic")
fun setLayoutHeight(v: View, height: Int) {
    v.layoutParams.height = height
    v.requestLayout()
}

@BindingAdapter("text_hint_color")
fun setTextHintColor(view: EditText, color: String) {
    view.setHintTextColor(Color.parseColor(color))
}

@BindingAdapter("set_hint")
fun setHint(view: TextView, hint: String) {
    view.hint = hint
}

@BindingAdapter("recyclerViewViewModel")
fun setRecyclerViewViewModel(recyclerView: RecyclerView, viewModel: RecyclerViewViewModel) {
    viewModel.setupRecyclerView(recyclerView)
}