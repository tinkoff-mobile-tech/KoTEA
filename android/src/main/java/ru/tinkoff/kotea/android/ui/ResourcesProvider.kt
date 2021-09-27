package ru.tinkoff.kotea.android.ui

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.Px
import androidx.annotation.StringRes

/**
 * Provider for local resources
 */
interface ResourcesProvider {

    fun getString(@StringRes resource: Int, vararg args: Any?): String

    fun getQuantityString(@PluralsRes resource: Int, quantity: Int, vararg args: Any?): String

    fun getStringArray(@ArrayRes resource: Int): Array<String>

    @ColorInt
    fun getColor(@ColorRes resource: Int): Int

    fun getDrawable(@DrawableRes drawableId: Int): Drawable

    @Px
    fun getDimensionPixelSize(@DimenRes dimen: Int): Int

    fun getColorStateList(@ColorRes resource: Int): ColorStateList?
}