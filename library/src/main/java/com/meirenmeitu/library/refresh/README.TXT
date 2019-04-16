1. 下拉刷新 (必须用 FrameLayout 作为根布局)

<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/fl_root"
    >
    <com.blcodes.views.refresh.BounceLayout
        android:id="@+id/bl"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#eee"
        >
        <android.support.v7.widget.RecyclerView
            android:id="@+id/rv_test"
            android:layout_width="match_parent"
            android:layout_height="match_parent"></android.support.v7.widget.RecyclerView>


    </com.blcodes.views.refresh.BounceLayout>
</FrameLayout>

public class RecyclerViewTestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_test);
        FrameLayout rootView = (FrameLayout)findViewById(R.id.fl_root);
        final BounceLayout bounceLayout = (BounceLayout)findViewById(R.id.bl);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_test);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RVTestAdapter adapter = new RVTestAdapter(this);
        recyclerView.setAdapter(adapter);
        bounceLayout.setBounceHandler(new NormalBounceHandler(),recyclerView);//设置滚动冲突的控制类
        bounceLayout.setEventForwardingHelper(new EventForwardingHelper() {//自定义事件分发处理
            @Override
            public boolean notForwarding(float downX, float downY, float moveX, float moveY) {
                return true;
            }
        });
        //设置刷新头，null意味着没有刷新头，不调用该函数意为着空
        bounceLayout.setHeaderView(new DefaultHeader(this),rootView);
        bounceLayout.setFooterView(new DefaultFooter(this),rootView);
        bounceLayout.setBounceCallBack(new BounceCallBack() {//刷新回调
            @Override
            public void startRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> data = new ArrayList<>();
                        for (int i = 0; i < 16; i++) {
                            data.add("新文本"+i);
                        }
                        adapter.setData(data);
                        bounceLayout.setRefreshCompleted();
                    }
                },2000);
            }

            @Override
            public void startLoadingMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> data = new ArrayList<>();
                        for (int i = 0; i < 6; i++) {
                            data.add("新增文本"+i);
                        }
                        adapter.addData(data);
                        bounceLayout.setLoadingMoreCompleted();
                    }
                },2000);
            }
        });

    }
}

2.回弹效果
<?xml version="1.0" encoding="utf-8"?>
<com.blcodes.views.refresh.BounceLayout
    android:id="@+id/bl"
    xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="match_parent"
    android:layout_height="match_parent">
    <android.support.v7.widget.RecyclerView
        android:id="@+id/rv_test"
        android:layout_width="match_parent"
        android:layout_height="match_parent"></android.support.v7.widget.RecyclerView>


</com.blcodes.views.refresh.BounceLayout>


public class RecyclerAndViewPagerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_vp_test);
        final BounceLayout bounceLayout = (BounceLayout)findViewById(R.id.bl);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_test);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RVAndVPAdapter adapter = new RVAndVPAdapter(this);
        recyclerView.setAdapter(adapter);
        bounceLayout.setBounceHandler(new NormalBounceHandler(),bounceLayout.getChildAt(0));
        bounceLayout.setEventForwardingHelper(new EventForwardingHelper() {
            @Override
            public boolean notForwarding(float downX, float downY, float moveX, float moveY) {
                return !ForwardingHelper.isXMore(downX,downY,moveX,moveY);
            }
        });
    }
}
