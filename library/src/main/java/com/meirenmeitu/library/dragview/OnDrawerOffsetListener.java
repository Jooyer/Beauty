package com.meirenmeitu.library.dragview;

import androidx.annotation.FloatRange;

/**
 * @Project 晓可广场
 * @Author 刘一召
 * @Date 2019/2/21
 * @Time 14:02
 * @Desc
 * @Copyright 2018 www.scshuimukeji.cn Inc. All rights reserved.
 * 注意: 本内容仅限于四川水木科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface OnDrawerOffsetListener {

    /**
     * 拖动偏移量变化为1-0  1为显示状态，0为关闭
     */
     void onDrawerOffset(@FloatRange(from = 0, to = 1) float offset);

}
