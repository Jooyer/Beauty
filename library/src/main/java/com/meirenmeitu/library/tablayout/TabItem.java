
package com.meirenmeitu.library.tablayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.TintTypedArray;

/**
 *  自定义 TabLayout
 */
public final class TabItem extends View {
    final CharSequence mText;
    final Drawable mIcon;
    final int mCustomLayout;

    public TabItem(Context context) {
        this(context, null);
    }

    @SuppressLint("RestrictedApi")
    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                com.google.android.material.R.styleable.TabItem);
        mText = a.getText(com.google.android.material.R.styleable.TabItem_android_text);
        mIcon = a.getDrawable(com.google.android.material.R.styleable.TabItem_android_icon);
        mCustomLayout = a.getResourceId(com.google.android.material.R.styleable.TabItem_android_layout, 0);
        a.recycle();
    }
}