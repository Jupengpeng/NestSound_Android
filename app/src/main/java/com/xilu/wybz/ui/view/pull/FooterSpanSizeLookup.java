package com.xilu.wybz.ui.view.pull;

import android.support.v7.widget.GridLayoutManager;

import com.xilu.wybz.view.pull.*;

/**
 * Created by Stay on 6/3/16.
 * Powered by June
 */
public class FooterSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private com.xilu.wybz.view.pull.BaseListAdapter adapter;
    private int spanCount;

    public FooterSpanSizeLookup(com.xilu.wybz.view.pull.BaseListAdapter adapter, int spanCount) {
        this.adapter = adapter;
        this.spanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {
        if (adapter.isLoadMoreFooter(position) || adapter.isSectionHeader(position)) {
            return spanCount;
        }
        return 1;
    }
}
