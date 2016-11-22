package com.xilu.wybz.ui.base;

import android.view.ViewGroup;

import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.section.SectionData;

/**
 * Created by hujunwei on 16/8/8.
 */
public abstract class BaseSectionListActivity<T> extends BaseListActivity<SectionData<T>> {
    protected static final int VIEW_TYPE_SECTION_HEADER = 111;
    protected static final int VIEW_TYPE_SECTION_CONTENT = 222;

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SECTION_HEADER) {
            return onCreateSectionHeaderViewHolder(parent);
        }
        return onCreateSectionViewHolder(parent, viewType);
    }

    protected abstract BaseViewHolder onCreateSectionViewHolder(ViewGroup parent, int viewType);


    @Override
    protected int getItemType(int position) {
        return mDataList.get(position).isHeader ? VIEW_TYPE_SECTION_HEADER : VIEW_TYPE_SECTION_CONTENT;
    }

    @Override
    protected boolean isSectionHeader(int position) {
        return mDataList.get(position).isHeader;
    }

    protected abstract BaseViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent);
}
