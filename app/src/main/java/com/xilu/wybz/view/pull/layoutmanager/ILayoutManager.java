package com.xilu.wybz.view.pull.layoutmanager;

import android.support.v7.widget.RecyclerView;
import com.xilu.wybz.view.pull.BaseListAdapter;


/**
 * Powered by June
 */
public interface ILayoutManager {
    RecyclerView.LayoutManager getLayoutManager();
    int findLastVisiblePosition();
    int findFirstVisiblePosition();
    void setUpAdapter(BaseListAdapter adapter);
}
