package com.meirenmeitu.common.other

import com.meirenmeitu.net.cover.ApiException
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-06-28
 * Time: 12:43
 */
abstract class RxObserver<T> : MaybeObserver<T> {
    override fun onSuccess(t: T) {
        success(t)
    }

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
        getDisposable(d)
    }

    override fun onError(e: Throwable) {
        when (e) {
            is ApiException -> { // 自定义运行时异常
                error(e.code, e.msg)
            }
            is HttpException -> { // 网络异常
                error(e.code(), e.message())
            }
            is ConnectException -> { // 连接异常
                error(550, e.message ?: "连接异常")
            }
            is SocketTimeoutException -> { // 连接异常
                error(551, e.message ?: "连接超时")
            }
            is JSONException -> { // 解析异常
                error(560, e.message ?: "解析异常")
            }
            is ParseException -> { // 解析异常
                error(560, e.message ?: "解析异常")
            }
            is AssertionError -> { // 返回数据没有data/list
                error(560, "返回数据没有data/list")
            }
            is UnknownHostException -> { // 无法解析该域名异常
                error(570, e.message ?: "无法连接")
            }
            else -> { // 其他未知异常
                error(1100, e.message ?: "未知错误")
            }
        }
    }

    abstract fun success(t: T)

    abstract fun getDisposable(d: Disposable)

    abstract fun error(code: Int, msg: String)

}