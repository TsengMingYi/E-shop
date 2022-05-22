package com.kitezeng.shopping.util;

import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    // 用来标记是否正在向上滑动
    private boolean isSlidingUpward = false;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) { //当前状态为停止滑动
            Log.d("TAG", "SCROLL_STATE_IDLE=" + (newState == RecyclerView.SCROLL_STATE_IDLE));
            if (!recyclerView.canScrollVertically(1)) { // 到达底部
                Log.d("TAG", "到达底部");
                onLoadMore();
//                Toast.makeText(this, "我是有底线的", Toast.LENGTH_SHORT).show();

            } else if (!recyclerView.canScrollVertically(-1)) { // 到达顶部
                Toast.makeText(recyclerView.getContext(), "到顶了", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "到顶了");
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // 大於0表示正在向上滑動，小於等於0表示停止或向下滑動
//        isSlidingUpward = dy > 0;
    }
    /**
     * 加载更多回调
     */
    public abstract void onLoadMore();
}
