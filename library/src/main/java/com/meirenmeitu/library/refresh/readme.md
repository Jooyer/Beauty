        // 大致用法如下

        snl_container.addHeader(DefaultHeaderView(mActivity))
        snl_container.addFooter(DefaultFooterView(mActivity))

        snl_container.setOnRefreshAndLoadListener(object : OnRefreshAndLoadListener() {
            override fun onRefresh(refreshLayout: PowerRefreshLayout) {
                Log.e("Test", "onRefresh==============")

                data.clear()
                for (i in 0 until 10) {
                    data.add("-----$i------")
                }

                refreshLayout.postDelayed({
                    rv_list.adapter?.notifyDataSetChanged()
                    snl_container.stopRefresh(true)
                }, 3000)
            }

            override fun onLoad(refreshLayout: PowerRefreshLayout) {
                Log.e("Test", "onLoad==============")
                for (i in data.size until data.size + 10) {
                    data.add("-----$i------")
                }
                refreshLayout.postDelayed({
                    rv_list.adapter?.notifyDataSetChanged()
                    snl_container.stopLoadMore(true)
                }, 3000)
            }
        })