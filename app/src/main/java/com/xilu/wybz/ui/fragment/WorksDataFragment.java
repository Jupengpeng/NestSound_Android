package com.xilu.wybz.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.InspireRecordViewHolder;
import com.xilu.wybz.adapter.WorksViewHolder;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.presenter.UserPresenter;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.SpacesItemDecoration;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.DividerItemDecoration;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/6/3.
 */
public class WorksDataFragment extends BaseListFragment<WorksData> implements IUserView {
    UserPresenter userPresenter;
    public static String TYPE = "type";
    public static String UID = "uid";
    public static String AUTHOR = "author";
    private int type;
    private int userId;
    private int deletePos;
    private String COME;
    private String author;
    private boolean isMe;
    private int workType;
    private String[] COMES = new String[]{"myrecord", "mysong", "mylyrics", "myfav"};
    private String[] COMESs = new String[]{"usersong", "userlyrics", "userfav"};

    @Override
    protected void initPresenter() {
        userPresenter = new UserPresenter(context, this);
        userPresenter.init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean hasPadding() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TYPE);
            userId = getArguments().getInt(UID);
            isMe = (userId == PrefsUtil.getUserId(context));
            if (!isMe) {
                COME = COMESs[type];
                type = type + 1;
            } else {
                COME = COMES[type];
                if (type == 0) {
                    type = 4;
                    workType = 3;
                } else {
                    workType = type;
                }
            }
            author = getArguments().getString(AUTHOR);
        }

    }

    public static WorksDataFragment newInstance(int type, int userId, String author) {
        WorksDataFragment tabFragment = new WorksDataFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putInt(UID, userId);
        bundle.putString(AUTHOR, author);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpacesItemDecoration(dip10);
    }

    @Override
    public void initView() {
        recycler.enablePullToRefresh(false);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        this.action = action;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
        userPresenter.loadData(userId, type, page++);
    }

    @Override
    public void setUserInfo(UserBean userBean) {
        EventBus.getDefault().post(new Event.UpdataUserBean(userBean, PrefsUtil.getUserId(context) == userId ? 1 : 2));
    }

    @Override
    public void showWorksData(List<WorksData> worksDataList) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        for (WorksData worksData : worksDataList) {
            if (type < 3)
                worksData.setAuthor(author);
            if (type == 0) {
                worksData.status = 4;
            } else if (type == 1) {
                worksData.status = 1;
            } else if (type == 2) {
                worksData.status = 2;
            } else if (type == 3) {
                worksData.status = worksData.type;
            }
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(worksDataList);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadFail() {
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadNoMore() {
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void deleteSuccess() {
        //更新本地当前播放音乐的缓存
        if (type == 3) {//删除了收藏 判断是否删除的是当前播放的这首歌
            WorksData worksData = PrefsUtil.getMusicData(context, mDataList.get(deletePos).itemid);
            if (worksData != null) {
                worksData.iscollect = 0;
                PrefsUtil.saveMusicData(context, worksData);
            }
        } else if (type == 1) {//删除我的歌曲成功 判断正在播放的是不是本首歌
            WorksData worksData = PrefsUtil.getMusicData(context, mDataList.get(deletePos).itemid);
            if (worksData != null) {//清除当前已删除歌曲的本地数据并停止播放
                PrefsUtil.clearMusicData(context, worksData.itemid);
                PlayMediaInstance.getInstance().release();
            }
        }
        removeItem(deletePos);
    }

    @Override
    public void deleteFail() {

    }

    @Override
    public void loadNoData() {
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    public void deleteWorksData(int pos) {
        deletePos = pos;
        userPresenter.delete(mDataList.get(pos).getItemid(), workType);
    }

    public void unfavWorksData(int pos) {
        deletePos = pos;
        WorksData worksData = mDataList.get(pos);
        userPresenter.unfav(worksData.itemid, worksData.uid, worksData.status);
    }

    public void removeData(WorksData worksData) {
        int index = -1;
        for (int i = 0; i < mDataList.size(); i++) {
            if (worksData.itemid == mDataList.get(i).itemid && worksData.status == mDataList.get(i).status) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            removeItem(index);
        }
    }

    public void showDeleteDialog(int pos) {
        new MaterialDialog.Builder(context)
                .title(getString(R.string.dialog_title))
                .content("确认删除该作品吗?")
                .positiveText("删除")
                .positiveColor(getResources().getColor(R.color.red))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        deletePos = pos;
                        if (type == 3) {
                            unfavWorksData(pos);
                        } else {
                            deleteWorksData(pos);
                        }
                    }
                }).negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        if (type == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_inspirerecord_item, parent, false);
            InspireRecordViewHolder holder = new InspireRecordViewHolder(view, context, mDataList, COME, isMe ? null : new InspireRecordViewHolder.OnDeleteListener() {
                @Override
                public void deletePos(int pos) {
                    showDeleteDialog(pos);
                }
            });
            return holder;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_work_list_item, parent, false);
            WorksViewHolder holder = new WorksViewHolder(view, context, mDataList, COME, !isMe ? null : new WorksViewHolder.OnDeleteListener() {
                @Override
                public void deletePos(int pos) {
                    if (type == 3) {
                        unfavWorksData(pos);
                    } else {
                        showDeleteDialog(pos);
                    }
                }
            });
            return holder;
        }
    }
}
