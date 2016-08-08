package com.xilu.wybz.ui.song;

import android.view.ViewGroup;

import com.xilu.wybz.bean.HotCatalog;
import com.xilu.wybz.presenter.HotCatalogPresenter;
import com.xilu.wybz.ui.IView.IHotCatalogView;
import com.xilu.wybz.ui.base.BaseSectionListActivity;
import com.xilu.wybz.view.pull.BaseViewHolder;
import java.util.List;

/**
 * Created by hujunwei on 16/8/8.
 */
public class HotCatalogActivity extends BaseSectionListActivity<HotCatalog> implements IHotCatalogView{
    HotCatalogPresenter hotCatalogPresenter;
    @Override
    protected void initPresenter() {
        hotCatalogPresenter = new HotCatalogPresenter(this,this);
        hotCatalogPresenter.init();
    }
    @Override
    public void initView() {
        setTitle("伴奏分类");
        hideRight();
    }

    @Override
    protected BaseViewHolder onCreateSectionViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void showHotCatalog(List<HotCatalog> hotCatalogList) {

    }

    @Override
    public void loadFail() {

    }

    @Override
    public void loadNoMore() {

    }

    @Override
    public void loadNoData() {

    }
}
