package ru.tinkoff.kotea.android.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

internal class ContextResourcesProvider(private val context: Context) : ResourcesProvider {

    override fun getString(resource: Int, vararg args: Any?): String {
        return context.resources.getString(resource, *args)
    }

    override fun getQuantityString(resource: Int, quantity: Int, vararg args: Any?): String {
        return context.resources.getQuantityString(resource, quantity, *args)
    }

    override fun getStringArray(resource: Int): Array<String> {
        return context.resources.getStringArray(resource)
    }

    override fun getColor(resource: Int): Int {
        return ContextCompat.getColor(context, resource)
    }

    override fun getDrawable(drawableId: Int): Drawable {
        return checkNotNull(context.getDrawable(drawableId)) {
            "Unable to get drawable from resources. Check passed drawable ID."
        }
    }

    override fun getDimensionPixelSize(dimen: Int): Int {
        return context.resources.getDimensionPixelSize(dimen)
    }

    override fun getColorStateList(resource: Int): ColorStateList? {
        return ContextCompat.getColorStateList(context, resource)
    }
}
