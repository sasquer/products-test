package com.sas.productstest.presentation.common

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.sas.productstest.R
import java.util.Locale

@BindingAdapter("price")
fun TextView.bindPrice(price: Double) {
    text = String.format(Locale.US, "$%.2f", price)
}

@BindingAdapter("discount")
fun TextView.bindDiscount(discount: Int) {
    if (discount > 0) {
        text = String.format(Locale.US, "-%d%%", discount)
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}

@BindingAdapter("availabilityText")
fun TextView.bindAvailabilityText(available: Boolean) {
    text = if (available) context.getString(R.string.in_stock)
    else context.getString(R.string.out_of_stock)
}

@BindingAdapter("availabilityColor")
fun TextView.bindAvailabilityColor(available: Boolean) {
    val colorRes = if (available) R.color.success_color else R.color.error_color
    setTextColor(ContextCompat.getColor(context, colorRes))
}
