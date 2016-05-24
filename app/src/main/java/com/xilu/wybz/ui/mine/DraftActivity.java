package com.xilu.wybz.ui.mine;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.xilu.wybz.R;
import com.xilu.wybz.ui.IView.IDraftView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.DensityUtil;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/5/24.
 */
public class DraftActivity extends ToolbarActivity implements IDraftView {


    @Bind(R.id.listView)
    SwipeMenuListView listView;
    @Bind(R.id.ll_nodata)
    LinearLayout llNodata;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_draft;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    @Override
    public void initView() {

        setTitle("草稿箱");

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem( getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFf, 0x49, 0x49)));
                deleteItem.setWidth(DensityUtil.dp2px(geContext(),70));
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(14);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        break;
                    case 1:
                        // delete
                        break;
                }
                return false;
            }
        });



        listView.setAdapter(new DraftAdapter(this));


    }


    public Context geContext(){
        return this;
    }

}
