package com.meirenmeitu.library.dragview;

import android.view.View;

import java.util.ArrayList;

/**
 * @Project 晓可广场
 * @Author 刘一召
 * @Date 2019/2/21
 * @Time 14:01
 * @Desc
 * @Copyright 2018 www.scshuimukeji.cn Inc. All rights reserved.
 * 注意: 本内容仅限于四川水木科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface OnDataListener {

    /**
     * 视图列表
     */
    ArrayList<View> getListView();

    /**
     * 数据列表
     */
    ArrayList<Object> getListData();

    /**
     * Frament列表，注意是Frament.class列表
     */
    ArrayList<Class> getListFragmentClass();

    void onPageSelected(int position);

    /**
     * true 代表运行执行onBackPressed，false则禁止onBackPressed
     */
    boolean onBackPressed();

    /**
     * 处理一些初始化操作
     */
    void init();

}
