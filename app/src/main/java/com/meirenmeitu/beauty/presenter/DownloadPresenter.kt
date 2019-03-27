package com.meirenmeitu.beauty.presenter

import com.meirenmeitu.beauty.model.DownloadModel
import com.meirenmeitu.beauty.view.DownloadActivity
import com.meirenmeitu.ui.mvp.BasePresenter

/**
 * @Project 晓可广场
 * @Author 刘一召
 * @Date  2019/2/27
 * @Time 14:36
 * @Desc
 * @Copyright 2018 www.scshuimukeji.cn Inc. All rights reserved.
 * 注意: 本内容仅限于四川水木科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
class DownloadPresenter(view:DownloadActivity): BasePresenter<DownloadActivity, DownloadModel>(view,DownloadModel()) {
}