package com.meirenmeitu.base.define

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.meirenmeitu.base.R

/**
 * Desc: 自定义 Toolbar ,格式如下:
 *
 * Author: Jooyer
 * Date: 2018-08-08
 * Time: 17:13
 */
class CustomToolbar(context: Context, attr: AttributeSet, defStyleAttr: Int) :
    RelativeLayout(context, attr, defStyleAttr) {


    lateinit var parent: View
    /**
     * 最左侧图标,默认显示
     */
    lateinit var iv_left_icon_menu: ImageView
    /**
     * 最左侧文本
     */
    lateinit var tv_left_name_menu: TextView
    /**
     * 中间文本
     */
    lateinit var tv_center_title_menu: TextView
    /**
     * 最右侧文本
     */
    lateinit var tv_right_name_menu: TextView
    /**
     * 最右侧图标,默认显示
     */
    lateinit var iv_right_icon_menu: ImageView
    /**
     * 最右侧图标2,默认隐藏
     */
    lateinit var iv_right_icon_menu2: ImageView
    /**
     * 底部分割线
     */
    lateinit var view_bottom_divider_menu: View


    constructor(context: Context, attr: AttributeSet) : this(context, attr, 0)

    init {
        initView()
        parseAttrs(context, attr)
    }

    private fun initView() {
        parent = LayoutInflater.from(context).inflate(R.layout.toolbar_custom, this, true)
        iv_left_icon_menu = parent.findViewById(R.id.iv_left_icon_menu)
        tv_left_name_menu = parent.findViewById(R.id.tv_left_name_menu)
        tv_center_title_menu = parent.findViewById(R.id.tv_center_title_menu)
        tv_right_name_menu = parent.findViewById(R.id.tv_right_name_menu)
        iv_right_icon_menu = parent.findViewById(R.id.iv_right_icon_menu)
        iv_right_icon_menu2 = parent.findViewById(R.id.iv_right_icon_menu2)
        view_bottom_divider_menu = parent.findViewById(R.id.view_bottom_divider_menu)

    }

    private fun parseAttrs(context: Context, attr: AttributeSet) {
        val arr = context.obtainStyledAttributes(attr, R.styleable.CustomToolbar)

        val background = arr.getColor(
            R.styleable.CustomToolbar_ct_view_background_color,
            ContextCompat.getColor(context, R.color.color_write)
        )

        val leftImageVisible = arr.getBoolean(R.styleable.CustomToolbar_ct_left_image_visible, true)
        val leftImageDrawable = arr.getDrawable(R.styleable.CustomToolbar_ct_left_image_drawable)
        val leftImageWidth = arr.getDimension(R.styleable.CustomToolbar_ct_left_image_width, dp2px(45F)).toInt()
        val leftImageHeight = arr.getDimension(R.styleable.CustomToolbar_ct_left_image_height, dp2px(50F)).toInt()
        val leftImagePadding = arr.getDimension(R.styleable.CustomToolbar_ct_left_image_padding, dp2px(8F)).toInt()
        val leftImageLeftMargin =
            arr.getDimension(R.styleable.CustomToolbar_ct_left_image_left_margin, dp2px(0F)).toInt()

        val leftTextVisible = arr.getBoolean(R.styleable.CustomToolbar_ct_left_text_visible, false)
        val leftTextInfo = arr.getText(R.styleable.CustomToolbar_ct_left_text_info)
        val leftTextSize = arr.getDimension(R.styleable.CustomToolbar_ct_left_text_size, dp2px(15F))
        val leftTextLeftMargin =
            arr.getDimension(R.styleable.CustomToolbar_ct_left_text_left_margin, dp2px(20F)).toInt()
        val leftTextColor = arr.getColor(
            R.styleable.CustomToolbar_ct_left_text_color,
            ContextCompat.getColor(context, R.color.color_write)
        )

        val centerTextInfo = arr.getText(R.styleable.CustomToolbar_ct_center_text_info)
        val centerTextDrawable = arr.getDrawable(R.styleable.CustomToolbar_ct_center_text_drawable)
        val centerTextDrawablePadding =
            arr.getDimension(R.styleable.CustomToolbar_ct_center_text_drawable_padding, dp2px(10F)).toInt()
        val centerTextSize = arr.getDimension(R.styleable.CustomToolbar_ct_center_text_size, dp2px(18F))
        val centerTextColor = arr.getColor(
            R.styleable.CustomToolbar_ct_center_text_color,
            ContextCompat.getColor(context, R.color.color_write)
        )

        val rightImageVisible = arr.getBoolean(R.styleable.CustomToolbar_ct_right_image_visible, true)
        val rightImageDrawable = arr.getDrawable(R.styleable.CustomToolbar_ct_right_image_drawable)
        val rightImageWidth = arr.getDimension(R.styleable.CustomToolbar_ct_right_image_width, dp2px(20F)).toInt()
        val rightImageHeight = arr.getDimension(R.styleable.CustomToolbar_ct_right_image_height, dp2px(20F)).toInt()
        val rightImagePadding = arr.getDimension(R.styleable.CustomToolbar_ct_right_image_padding, dp2px(5F)).toInt()
        val rightImageRightMargin =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_image_right_margin, dp2px(20F)).toInt()

        val rightImage2Visible = arr.getBoolean(R.styleable.CustomToolbar_ct_right_image2_visible, false)
        val rightImage2Drawable = arr.getDrawable(R.styleable.CustomToolbar_ct_right_image2_drawable)
        val rightImage2Width = arr.getDimension(R.styleable.CustomToolbar_ct_right_image2_width, dp2px(20F)).toInt()
        val rightImage2Height = arr.getDimension(R.styleable.CustomToolbar_ct_right_image2_height, dp2px(20F)).toInt()
        val rightImage2Padding = arr.getDimension(R.styleable.CustomToolbar_ct_right_image2_padding, dp2px(5F)).toInt()
        val rightImage2RightMargin =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_image2_right_margin, dp2px(30F)).toInt()

        val rightTextVisible = arr.getBoolean(R.styleable.CustomToolbar_ct_right_text_visible, false)
        val rightTextInfo = arr.getText(R.styleable.CustomToolbar_ct_right_text_info)
        val rightTextSize = arr.getDimension(R.styleable.CustomToolbar_ct_right_text_size, dp2px(15F))
        val rightTextRightMargin =
            arr.getDimension(R.styleable.CustomToolbar_ct_right_text_right_margin, dp2px(20F)).toInt()
        val rightTextColor = arr.getColor(
            R.styleable.CustomToolbar_ct_right_text_color,
            ContextCompat.getColor(context, R.color.color_write)
        )

        val bottomDividerVisible = arr.getBoolean(R.styleable.CustomToolbar_ct_bottom_divider_visible, false)
        val bottomDividerColor = arr.getColor(
            R.styleable.CustomToolbar_ct_bottom_divider_color,
            ContextCompat.getColor(context, R.color.color_EEEEEE)
        )

        parent.setBackgroundColor(background)

        iv_left_icon_menu.visibility = if (leftImageVisible) View.VISIBLE else View.GONE
        if (null != leftImageDrawable) {
            iv_left_icon_menu.setImageDrawable(leftImageDrawable)
        }
        if (leftImageVisible) {
            iv_left_icon_menu.setOnClickListener {
                if (context is Activity) {
                    context.finish()
                }
            }
        }
        val leftImageLp: RelativeLayout.LayoutParams = iv_left_icon_menu.layoutParams as LayoutParams
        leftImageLp.width = leftImageWidth
        leftImageLp.height = leftImageHeight
        leftImageLp.leftMargin = leftImageLeftMargin
        iv_left_icon_menu.setPadding(leftImagePadding, leftImagePadding, leftImagePadding, leftImagePadding)
        iv_left_icon_menu.layoutParams = leftImageLp

        tv_left_name_menu.visibility = if (leftTextVisible) View.VISIBLE else View.GONE
        if (!TextUtils.isEmpty(leftTextInfo)) {
            tv_left_name_menu.text = leftTextInfo
            tv_left_name_menu.paint.textSize = leftTextSize
            tv_left_name_menu.setTextColor(leftTextColor)
        }

        val leftTextLp: RelativeLayout.LayoutParams = tv_left_name_menu.layoutParams as LayoutParams
        leftTextLp.leftMargin = leftTextLeftMargin
        tv_left_name_menu.layoutParams = leftTextLp

        if (!TextUtils.isEmpty(centerTextInfo)) {
            tv_center_title_menu.text = centerTextInfo
            tv_center_title_menu.paint.textSize = centerTextSize
            tv_center_title_menu.setTextColor(centerTextColor)
        }

//        tv_center_title_menu.setShadowLayer(0.15F, 0.2F, 0.2F, ContextCompat.getColor(context, R.color.color_write))

        if (null != centerTextDrawable) {
            tv_center_title_menu.setCompoundDrawablesWithIntrinsicBounds(centerTextDrawable, null, null, null)
            tv_center_title_menu.compoundDrawablePadding = centerTextDrawablePadding
        }

        iv_right_icon_menu.visibility = if (rightImageVisible) View.VISIBLE else View.GONE
        if (null != rightImageDrawable) {
            iv_right_icon_menu.setImageDrawable(rightImageDrawable)
        }
        val rightImageLp: RelativeLayout.LayoutParams = iv_right_icon_menu.layoutParams as LayoutParams
        rightImageLp.width = rightImageWidth
        rightImageLp.height = rightImageHeight
        rightImageLp.rightMargin = rightImageRightMargin
        iv_right_icon_menu.setPadding(rightImagePadding, rightImagePadding, rightImagePadding, rightImagePadding)
        iv_right_icon_menu.layoutParams = rightImageLp


        iv_right_icon_menu2.visibility = if (rightImage2Visible) View.VISIBLE else View.GONE
        if (null != rightImage2Drawable) {
            iv_right_icon_menu2.setImageDrawable(rightImage2Drawable)
        }
        val rightImageLp2: RelativeLayout.LayoutParams = iv_right_icon_menu2.layoutParams as LayoutParams
        rightImageLp2.width = rightImage2Width
        rightImageLp2.height = rightImage2Height
        rightImageLp2.rightMargin = rightImage2RightMargin
        iv_right_icon_menu2.setPadding(rightImage2Padding, rightImage2Padding, rightImage2Padding, rightImage2Padding)
        iv_right_icon_menu2.layoutParams = rightImageLp2

        tv_right_name_menu.visibility = if (rightTextVisible) View.VISIBLE else View.GONE
        if (!TextUtils.isEmpty(rightTextInfo)) {
            tv_right_name_menu.text = rightTextInfo
            tv_right_name_menu.paint.textSize = rightTextSize
            tv_right_name_menu.setTextColor(rightTextColor)
        }
        val rightTextLp: RelativeLayout.LayoutParams = tv_right_name_menu.layoutParams as LayoutParams
        rightTextLp.rightMargin = rightTextRightMargin
        tv_right_name_menu.layoutParams = rightTextLp

        view_bottom_divider_menu.visibility = if (bottomDividerVisible) View.VISIBLE else View.GONE
        view_bottom_divider_menu.setBackgroundColor(bottomDividerColor)

        arr.recycle()
    }

    fun setRightImageListener(listener: View.OnClickListener) {
        iv_right_icon_menu.setOnClickListener(listener)
    }

    fun setRightImage2Listener(listener: View.OnClickListener) {
        iv_right_icon_menu2.setOnClickListener(listener)
    }

    fun setRightTextListener(listener: View.OnClickListener) {
        tv_right_name_menu.setOnClickListener(listener)
    }

    fun setRightTextVisible(visable: Int) {
        tv_right_name_menu.visibility = visable
    }

    fun setcenterText(text: String) {
        tv_center_title_menu.text = text
    }

    fun setRightText(text: String) {
        tv_right_name_menu.text = text
    }

    fun setRightImage2(resource: Int) {
        iv_right_icon_menu2.setImageResource(resource)
    }

    fun setLeftImageVisible(visible: Int) {
        iv_left_icon_menu.visibility = visible
    }

    fun setRightImage2Visible(visible: Int) {
        iv_right_icon_menu2.visibility = visible
    }


    private fun dp2px(def: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, def, context.resources.displayMetrics)
    }

}