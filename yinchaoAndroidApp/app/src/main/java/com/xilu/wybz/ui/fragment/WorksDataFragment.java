package com.xilu.wybz.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.InspireRecordViewHolder;
import com.xilu.wybz.adapter.WorksViewHolder;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.UserInfoBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.presenter.UserPresenter;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


/**
 * Created by hujunwei on 16/6/3.
 */
public class WorksDataFragment extends BaseListFragment<WorksData> implements IUserView {
    UserPresenter userPresenter;
    public static String TYPE = "type";
    public static String UID = "uid";
    public static String AUTHOR = "author";
    private int type;//1=歌曲，2=歌词，3=收藏,4=灵感记录（加载）
    private int userId;
    private int selectPos;
    private String COME;
    private String author;
    private boolean isMe;
    private int workType;//type 1=歌曲，2=歌词，3=灵感记录（删除作品的type）
    private String[] MYCOMES = new String[]{"mysong", "mylyrics", "myfav", "myrecord"};
    private String[] OTHERCOMES = new String[]{"usersong", "userlyrics", "userfav"};//他人主页
    private boolean isFirst;
    private boolean isFirstTab;
    @Override
    protected void initPresenter() {
        EventBus.getDefault().register(this);
        userPresenter = new UserPresenter(context, this);
        userPresenter.init();
    }

    public void loadData() {
        if (isFirst) return;
        else isFirst = true;
        recycler.setRefreshing();
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TYPE);
            userId = getArguments().getInt(UID);
            isMe = (userId == PrefsUtil.getUserId(context));
            if (type == 0) isFirstTab = true;
            if (!isMe) {
                COME = OTHERCOMES[type];
            } else {
                COME = MYCOMES[type];
                if(type==0||type==1){//歌曲歌词 需要+1
                    workType = type+1;
                }else if(type==3){//灵感记录
                    workType = type;
                }
            }
            type = type + 1;
            author = getArguments().getString(AUTHOR);
        }

    }

    protected ILayoutManager getLayoutManager() {
        MyLinearLayoutManager myLinearLayoutManager = new MyLinearLayoutManager(getActivity().getApplicationContext(), OrientationHelper.VERTICAL, false);
        return myLinearLayoutManager;
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


    @Override
    public void initView() {
//        recycler.enablePullToRefresh(false);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        if (recycler == null) {
            return;
        }
        if (isFirstTab) {
            recycler.setRefreshing();
            isFirst = true;
        }
    }

    public void reSet() {
        userId = PrefsUtil.getUserId(context);
        author = PrefsUtil.getUserInfo(context).name;
        isMe = (userId == PrefsUtil.getUserId(context));
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        userPresenter.loadData(userId, type, page++);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event.LoginOutEvent event) {
        isFirst = false;
        if (mDataList != null && mDataList.size() != 0) {
            page = 1;
            mDataList.clear();
            adapter.notifyDataSetChanged();
            recycler.getRecyclerView().requestLayout();
        }
    }

    @Override
    public void setUserInfo(UserBean userBean) {
        EventBus.getDefault().post(new Event.UpdataUserBean(userBean, isMe ? 1 : 2));
    }

    @Override
    public void setUserInfoBean(UserInfoBean userBean) {
        EventBus.getDefault().post(new Event.UpdataUserInfoBean(userBean, isMe ? 1 : 2));
    }

    @Override
    public void showWorksData(List<WorksData> worksDataList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) return;
                if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
                    mDataList.clear();
                }
                for (WorksData worksData : worksDataList) {
                    if (type < 3)
                        worksData.setAuthor(author);
                    if (type == 0) {
                        worksData.type = 4;
                    } else if (type == 1) {
                        worksData.type = 1;
                    } else if (type == 2) {
                        worksData.type = 2;
                    }
                    mDataList.add(worksData);
                }
                llNoData.setVisibility(View.GONE);
                recycler.enableLoadMore(true);
                recycler.getRecyclerView().requestLayout();
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        }, 600);
    }

    public void updateList() {
        if (recycler == null) {
            return;
        }
        recycler.onRefresh();
    }

    @Override
    public void loadFail() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadNoMore() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    public void updateNum(WorksData worksData, int type) {
        if (isDestroy||mDataList==null) return;
        int index = -1;
        for (int i = 0; i < mDataList.size(); i++) {
            if (worksData.itemid == mDataList.get(i).itemid && worksData.type == mDataList.get(i).type) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            if (type == 2) {
                mDataList.get(index).setZannum(worksData.zannum);
            } else if (type == 1) {
                mDataList.get(index).setFovnum(worksData.fovnum);
            }
            updateItem(index);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event.UpdateWorkNum event) {
        updateNum(event.getWorksData(), event.getType());
    }


    @Override
    public void deleteSuccess() {
        if (isDestroy) return;
        //更新本地当前播放音乐的缓存
        if (type == 3) {//删除了收藏 判断是否删除的是当前播放的这首歌
            WorksData worksData = PrefsUtil.getMusicData(context, mDataList.get(selectPos).itemid);
            if (worksData != null) {
                worksData.iscollect = 0;
                PrefsUtil.saveMusicData(context, worksData);
            }
        } else if (type == 1) {//删除我的歌曲成功 判断正在播放的是不是本首歌
            WorksData worksData = PrefsUtil.getMusicData(context, mDataList.get(selectPos).itemid);
            if (worksData != null) {//清除当前已删除歌曲的本地数据并停止播放
                PrefsUtil.clearMusicData(context, worksData.itemid);
                PlayMediaInstance.getInstance().release();
            }
        }
        removeItem(selectPos);
        EventBus.getDefault().post(new Event.UpdateWorksNum(type,-1));
    }

    @Override
    public void deleteFail() {
    }

    @Override
    public void updateSuccess() {
        mDataList.get(selectPos).status = 1 - mDataList.get(selectPos).status;
        updateItem(selectPos);
        ToastUtils.toast(context,"设置成功！");
    }

    @Override
    public void updateFail() {

    }

    @Override
    public void loadNoData() {
        if (isDestroy) return;
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    //移除某个item
    public void removeByItemid(String itemid) {
        int index = -1;
        for (int i = 0; i < mDataList.size(); i++) {
            if (itemid.equals(mDataList.get(i).itemid)) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            removeItem(index);
        }
    }

    //更新某个item
    public void updateData(WorksData worksData) {
        if (mDataList!=null&&mDataList.size() > 0) {
            int index = -1;
            for (int i = 0; i < mDataList.size(); i++) {
                if (worksData.itemid == mDataList.get(i).itemid && worksData.status == mDataList.get(i).status) {
                    index = i;
                    break;
                }
            }
            if (index > -1) {
                updateItem(index, worksData);
            }
        }
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

    //删除作品
    public void deleteWorksData(int pos) {
        selectPos = pos;
        if (pos >= 0 && pos < mDataList.size()) {
            userPresenter.delete(mDataList.get(pos).getItemid(), workType);
        }
    }

    //取消收藏作品
    public void unfavWorksData(int pos) {
        selectPos = pos;
        if (pos >= 0 && pos < mDataList.size()) {
            WorksData worksData = mDataList.get(pos);
            userPresenter.unfav(worksData.itemid, worksData.uid, worksData.type);
        }
    }

    //改变作品的公开状态
    public void changeWorksState(int pos) {
        selectPos = pos;
        if (pos >= 0 && pos < mDataList.size()) {
            WorksData worksData = mDataList.get(pos);
            int status = 1 - worksData.status;
            userPresenter.updateWorksState(worksData.itemid, type, status);
        }
    }

    //删除作品提示
    public void showDeleteDialog(int pos) {
        new MaterialDialog.Builder(context)
                .title(getString(R.string.dialog_title))
                .content("确认删除该作品吗?")
                .positiveText("删除")
                .negativeText("取消")
                .positiveColor(getResources().getColor(R.color.red))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        selectPos = pos;
                        deleteWorksData(pos);
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            }
        }).show();
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        if (type == 4) {//灵感记录
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_inspirerecord_item, parent, false);
            InspireRecordViewHolder holder = new InspireRecordViewHolder(view, context, mDataList, COME,
                    !isMe ? null : new InspireRecordViewHolder.OnItemClickListener() {
                        @Override
                        public void onClick(int pos, int which) {
                            if (which == 0) {
                                showDeleteDialog(pos);
                            }
                        }
                    });
            return holder;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_work_list_item, parent, false);
            WorksViewHolder holder = new WorksViewHolder(view, context, mDataList, COME,
                    !isMe ? null : new WorksViewHolder.OnItemClickListener() {
                        @Override
                        public void onClick(int pos, int which) {
                            if (type == 3) {//取消收藏的时候 不提示 直接取消
                                if (which == 0) {
                                    unfavWorksData(pos);
                                }
                            } else {
                                if (which == 0) {
                                    changeWorksState(pos);
                                } else if (which == 1) {
                                    showDeleteDialog(pos);
                                }
                            }
                        }
                    });
            return holder;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (userPresenter != null)
            userPresenter.cancelRequest();
        EventBus.getDefault().unregister(this);
    }
}