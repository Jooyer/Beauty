package com.meirenmeitu.library.rxbind;

import android.view.View;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

import static com.meirenmeitu.library.rxbind.Preconditions.checkNotNull;
import static com.meirenmeitu.library.rxbind.Preconditions.checkUiThread;


/**
 * Desc: https://www.jianshu.com/p/01aa6e93c98c
 * Author: Jooyer
 * Date: 2018-09-21
 * Time: 15:22
 */
public class RxView {
     private static CompositeDisposable sCompositeDisposable = new CompositeDisposable();


    /**
     * 防止重复点击
     *
     * @param targets 目标view
     * @param action  监听器
     */
    public static void setOnClickListeners(final Action1<View> action, @NonNull View... targets) {
        for (View view : targets) {
            Disposable disposable = RxView.onClick(view)
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<View>() {
                        @Override
                        public void accept(@NonNull View view) throws Exception {
                            action.onClick(view);
                        }
                    });

//            sCompositeDisposable.add(disposable);
        }
    }

    /**
     * 监听onclick事件防抖动
     *
     * @param view
     * @return
     */
    @CheckResult
    @NonNull
    private static Observable<View> onClick(@NonNull View view) {
        checkNotNull(view, "view == null");
        return Observable.create(new ViewClickOnSubscribe(view));
    }

//    @CheckResult
//    @NonNull
//    public static <T extends Adapter> Observable<AdapterViewItemClickEvent> itemClickEvents(
//            @NonNull RecyclerAdapter<?> view) {
//        checkNotNull(view, "view == null");
//        return Observable.create(new AdapterViewItemClickEventOnSubscribe(view));
//    }

    /**
     * onclick事件防抖动
     * 返回view
     */
    private static class ViewClickOnSubscribe implements ObservableOnSubscribe<View> {
        private View view;

        public ViewClickOnSubscribe(View view) {
            this.view = view;
        }

        @Override
        public void subscribe(@NonNull final ObservableEmitter<View> e) throws Exception {
            checkUiThread();

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!e.isDisposed()) {
                        e.onNext(view);
                    }
                }
            };
            view.setOnClickListener(listener);
        }
    }

    /**
     *  点击事件应该没有内存泄漏问题
     */
    public static void disposeBindClick() {
//       sCompositeDisposable.dispose();
    }


    /**
     * A one-argument action. 点击事件转发接口
     *
     * @param <T> the first argument type
     */
    public interface Action1<T> {
        void onClick(T view);
    }


}
