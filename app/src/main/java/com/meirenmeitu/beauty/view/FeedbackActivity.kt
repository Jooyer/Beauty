package com.meirenmeitu.beauty.view

import android.view.View
import androidx.core.content.ContextCompat
import com.meirenmeitu.base.utils.SelectorFactory
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.presenter.FeedbackPresenter
import com.meirenmeitu.library.rxbind.RxView
import com.meirenmeitu.library.utils.DensityUtils
import com.meirenmeitu.library.utils.KeyBoardUtil
import com.meirenmeitu.library.utils.StatusBarUtil
import com.meirenmeitu.net.utils.Base64Util
import com.meirenmeitu.net.utils.RSAUtil
import com.meirenmeitu.ui.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_feedback.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

/**
 * 意见反馈
 */
class FeedbackActivity : BaseActivity<FeedbackPresenter>() {

    private val publicKeyStr =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAznKMTPFM/0MUJ6L/ MmOWub05L/B/rsvTE/7+l85dR5wwxmWIYbo60NkBPpnmIID+hlNNydXsKGvL pUw/BnKNdyhIVv5LtKhmwAP5zQm7HQUKnTkiUh801N/dxeQP5Ma2xZH8HorL OEAlniWwwAt7ap46S0jbDejERr/wR9k3TqqQy28UhATBy+YakCnJg+u3Enr2 jSYmEWJ2MIMZ/J51H6p/TZBUA9HXVlMj0WY5uli9ELZa8OrCy2isQYbGzFj9 mysykgZtkPGFT2JvNUoISbK0jGUzhbjFX4SX8JztmEYdYd8tM5pp3l7zOC9J JERZYUZLjZoZJ09jUUGnFaGulQIDAQAB"


    private val privateKeyStr =
        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDOcoxM8Uz/ QxQnov8yY5a5vTkv8H+uy9MT/v6Xzl1HnDDGZYhhujrQ2QE+meYggP6GU03J 1ewoa8ulTD8Gco13KEhW/ku0qGbAA/nNCbsdBQqdOSJSHzTU393F5A/kxrbF kfweiss4QCWeJbDAC3tqnjpLSNsN6MRGv/BH2TdOqpDLbxSEBMHL5hqQKcmD 67cSevaNJiYRYnYwgxn8nnUfqn9NkFQD0ddWUyPRZjm6WL0Qtlrw6sLLaKxB hsbMWP2bKzKSBm2Q8YVPYm81SghJsrSMZTOFuMVfhJfwnO2YRh1h3y0zmmne XvM4L0kkRFlhRkuNmhknT2NRQacVoa6VAgMBAAECggEAIv2/UftENa/E5sL1 xjCjB3BPFRUKTzI04JrDxC8yGEUc70OeThImjed6EoY7Px8eBN5xvNgJYwCC T84QkKMx3xRwzm3jnwc8AyepW1te35hPKD7a1lh7t+kJydTR7NDRgcMwnGyh 7TLSLW/t+V742CVhdAkxN7ZQLj2yGDt06Fwqkxu/KGLx6UtZ2T9wZgp0BVfu WHfzEfMdO9tMxfp6bbDZ0uKhpr+UPB/94XSjFssGsSRpVjVKVwpta96aZFhh 1P2tAjM9PfEkrQ9yAEe6Mxe5sw2vhUcBUfgVgI8qVGBPkOwZIXX7ltvN51qj a6vZXTsrHWSM3Cph/PjNuvx9sQKBgQDwti+cz58MzXt3NwysnwufobUJPw9V E5ByZGiLr8+JH9IxXnA3ivl+3S3M4oyPx3xmBkKdUfZnhBRB0kNUw+SzrYI5 7OiwrpZNH06+t1Vj1h/u1rYcSZpoyhsvHxJ3t0R/JvfJcoo+QBQcBaUa0JO6 BeHpwzzryLLwAGq7jw5xcwKBgQDbjz+ZiKcbIVNtUDv1CSF++EmTUo2njmjZ gflavRuccGug6EsOtF5oPmsT+QZchpX3zxronH9El+HpC4SRlbYPLKJhR1uD 4tq+iz7yOhEpr+kh1NtfcLLQKYWhZ+Fq08NNQzVU4FWnfyXDrGWchyV9f/pE pZbtAePArK83xKg91wKBgAk70+O+CyngkAXotLHCgB2lhF5QbvQihf39YA1L tzhZYiTGrKNUz3FQZrtfTWkOi/UV9GQ4iq054dUMjcyonxNNaptyPRLZ/6YB t3GVcs8bnan5zo6TPDsc1aIfHQQ+oYAHLKYroKX2q07P4J+AtMoEA8CF3sZk soiGW5HKURGFAoGBALDs5zhGuToQidTdweL8P6S8WTXBYWrzRCR6K0DmgLh4 kxJv/gPUbvsbyVen5Wtr/K9bkfh+G8Rfi+kqoeJbPQFPW25+fQ1zIghH1yqE hk4MdhYQG9L10vIRc6CUQD7AdHHvRaumtv8pY3ohkyQmd5yBI2Br+4nzat9Z iDytWVGzAoGAC/DsNrg+EnqgmTgChhvguhyc8l88USIWwdBQ4MDMR/lE4LxE 6RJozgX0gkx3HPJWywwLj3c71qIkenkDu6h3vnbbL+NjrdtbzXejNkI0615j zKt1xj7G7X/L10jmeoMyiqKkHA3NC52PC5HTe3bkPQgW2bowbxZoySPCREZl a/I="

    override fun needUseImmersive() = true

    override fun requestWindowFeature() {
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_theme_color))
    }

    override fun createPresenter() = FeedbackPresenter(this)
    override fun getLayoutId() = R.layout.activity_feedback

    override fun setLogic() {
        btn_submit_feedback.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.main_theme_color))
            .setCornerRadius(DensityUtils.dpToPx(5))
            .create()
    }

    override fun onResume() {
        super.onResume()
        KeyBoardUtil.openKeyboard(this, et_content_feedback)
    }

    override fun bindEvent() {
        RxView.setOnClickListeners(this, btn_submit_feedback, btn_submit_feedback2)
    }

    var bytes: ByteArray? = "Hello".toByteArray()

    val pair = RSAUtil.generateRSAKeyPair("Molue")

    override fun onClick(view: View) {
        // 公钥
        val publicKey: RSAPublicKey = pair.public as RSAPublicKey
        println("RSAUtil(公钥)=============: ${Base64Util.encode(publicKey.encoded)}")
        // 私钥
        val privateKey: RSAPrivateKey = pair.private as RSAPrivateKey
        println("RSAUtil(私钥)=============: ${Base64Util.encode(privateKey.encoded)}")

        when (view.id) {
            R.id.btn_submit_feedback -> {
                bytes = RSAUtil.encryptByPublicKey("Hello".toByteArray(), Base64Util.decode(publicKeyStr))
            }
            R.id.btn_submit_feedback2 -> {
                bytes?.let {
                    println(
                        "================= ${String(
                            RSAUtil.decryptByPrivateKey(
                                it,
                                Base64Util.decode(privateKeyStr)
                            )
                        )}"
                    )
                }
            }
        }
    }

}