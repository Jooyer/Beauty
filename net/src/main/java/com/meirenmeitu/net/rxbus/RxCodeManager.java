package com.meirenmeitu.net.rxbus;

import androidx.annotation.Keep;

/**
 * 将 RxBus 使用的 code 定义在此处,方便统一管理
 * Created by Jooyer on 2017/6/3
 */
@Keep
public class RxCodeManager {
    // 预留 0 - 1000 ,作为基本事件
    // 广告显示完毕,跳转到主页
    public static final int RX_CODE_SHOW_AD_FINISHED = 1001;

    // 点击个人菜单,打开侧边栏抽屉
    public static final int RX_CODE_OPEN_DRAWER_LAYOUT = 1002;





    // 需要同步用户信息
    public static final int RX_CODE_NEED_SYNC_USER_INFO = 10001;



}
