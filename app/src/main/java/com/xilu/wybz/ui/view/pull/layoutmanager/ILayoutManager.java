package com.xilu.wybz.ui.view.pull.layoutmanager;

import android.support.v7.widget.RecyclerView;
import com.xilu.wybz.view.pull.BaseListAdapter;


/**
 * Created by Stay on 5/3/16.
 * Powered by June
 */
public interface ILayoutManager {
    RecyclerView.LayoutManager getLayoutManager();
    int findLastVisiblePosition();
    void setUpAdapter(BaseListAdapter adapter);
}
