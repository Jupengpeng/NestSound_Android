package com.xilu.wybz.ui.cooperation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperationBean;
import com.xilu.wybz.bean.PreinfoBean;
import com.xilu.wybz.presenter.CooperationPresenter;
import com.xilu.wybz.ui.IView.ICooperationView;
import com.xilu.wybz.ui.fragment.BaseListFragment;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.ui.mine.OtherUserCenterActivity;
import com.xilu.wybz.ui.mine.UserCenterActivity;
import com.xilu.wybz.ui.song.HotCatalogActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.CircleImageView;
import com.xilu.wybz.view.MyRecyclerView;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.List;

import butterknife.Bind;

import static com.umeng.socialize.Config.dialog;

/**
 * Created by Administrator on 2016/10/19.
 */

public class CooperationFragment extends BaseListFragment<CooperationBean> implements ICooperationView {


    private CooperationPresenter cooperationPresenter;

    private int did;//合作需求ID

    private View stub;

    /**
     * 调用页面.
     */
    @Override
    protected void initPresenter() {
        recycler.getRecyclerView().setBackgroundColor(Color.parseColor("#ffffff"));

        cooperationPresenter = new CooperationPresenter(context, this);
        cooperationPresenter.init();
    }

    /**
     * initView.
     */
    @Override
    public void initView() {

        ViewStub viewStub = (ViewStub)mRootView.findViewById(R.id.view_stub_bottom);
        stub = viewStub.inflate().findViewById(R.id.more_add);

        stub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrefsUtil.getUserId(context) == 0) {
                    startLoginPage();
                } else {
                    Intent intent = new Intent(getActivity(), CooperaPublishActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initDialog(int pos) {

        dialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.hezuotishidialig, null);
        Button positive_bt = (Button) layout.findViewById(R.id.cancle_bt);
        Button cancle_bt = (Button) layout.findViewById(R.id.positive_bt);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        cancle_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                did = mDataList.get(pos).getDemandInfo().getId();
                cooperationPresenter.getPreinfo(did);

            }
        });
        positive_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        cooperationPresenter.getCooperationList(page++);
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cooperation_item, parent, false);
        BaseViewHolder viewHolder = new CooperViewHolder(view);
        return viewHolder;
    }

    /**
     * CooperViewHolder.
     */
    class CooperViewHolder extends BaseViewHolder {

        @Bind(R.id.coopera_head_iv)
        CircleImageView cooperaHeadIv;
        @Bind(R.id.coopera_tv_name)
        TextView cooperaTvName;
        @Bind(R.id.coopera_tv_time)
        TextView cooperaTvTime;
        @Bind(R.id.coopera_bt)
        Button cooperaBt;
        @Bind(R.id.ll_jump2)
        LinearLayout llJump2;
        @Bind(R.id.view)
        View view;
        @Bind(R.id.coopera_tv_lyricsname)
        TextView cooperaTvLyricsname;
        @Bind(R.id.coopera_tv_lyricscontent)
        TextView cooperaTvLyricscontent;
        @Bind(R.id.coopera_tv_require)
        TextView cooperaTvRequire;
        @Bind(R.id.coopera_comment_rectclerview)
        MyRecyclerView cooperaCommentRectclerview;
        @Bind(R.id.coopera_commentnum_tv)
        TextView cooperaCommentnumTv;
        @Bind(R.id.coopera_worknum_tv)
        TextView cooperaWorknumTv;
        @Bind(R.id.ll_jump)
        LinearLayout llJump;

        CooperationBean bean;




        public CooperViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            bean = mDataList.get(position);

            loadImage(bean.getUserInfo().getHeadurl(), cooperaHeadIv);
            cooperaTvName.setText(bean.getUserInfo().getNickname());
            cooperaTvTime.setText(DateFormatUtils.formatX1(bean.getDemandInfo().getCreatetime()));

            cooperaTvLyricsname.setText(bean.getDemandInfo().getTitle());
            cooperaTvRequire.setText(bean.getDemandInfo().getRequirement());
            cooperaTvLyricscontent.setText(bean.getDemandInfo().getLyrics());

            cooperaCommentnumTv.setText("更多" + bean.getDemandInfo().getCommentnum() + "条留言");
            cooperaWorknumTv.setText("已有" + bean.getDemandInfo().getWorknum() + "位巢人参与合作");

            List<CooperationBean.CommentListBean> commentBeanList;
            commentBeanList = bean.getCommentList();
            CooperaCommentAdapter adapter = new CooperaCommentAdapter(commentBeanList, context);

            cooperaCommentRectclerview.setAdapter(adapter);

            llJump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PrefsUtil.getUserId(context) == 0) {
                        startLoginPage();
                    } else {
                        Intent intent = new Intent(getActivity(), CooperaDetailesActivity.class);
                        if (bean.getUserInfo().getUid() == PrefsUtil.getUserId(context)) {
                            intent.putExtra("type", 2);
                        } else {
                            intent.putExtra("type", 1);
                        }
                        intent.putExtra("did", bean.getId());
                        startActivity(intent);
                    }
                }
            });

            cooperaHeadIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PrefsUtil.getUserId(context) == 0) {
                        startLoginPage();
                    } else {
                        int uid = bean.getUserInfo().getUid();
                        String username = bean.getUserInfo().getNickname();
                        if (uid == PrefsUtil.getUserId(context)) {
                            Intent intent1 = new Intent(getActivity(), UserCenterActivity.class);
                            startActivity(intent1);
                        } else {
                            OtherUserCenterActivity.toUserInfoActivity(context, uid, username);
                        }
                    }

                }
            });

            cooperaBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PrefsUtil.getUserId(context) == 0) {
                        startLoginPage();
                    } else {
                        initDialog(position);
                    }
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }


    @Override
    public void showCooperation(List<CooperationBean> cooperationBeanList) {

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) return;

                recycler.onRefreshCompleted();
                llNoData.setVisibility(View.GONE);
                recycler.enableLoadMore(true);
                mDataList.addAll(cooperationBeanList);
                adapter.notifyDataSetChanged();

                checkData();
            }
        }, 600);
    }

    @Override
    public void noMoreData() {

        recycler.enableLoadMore(false);
    }

    @Override
    public void showpreinfoBean(PreinfoBean preinfoBean) {
        Intent intent = new Intent(getActivity(), HotCatalogActivity.class);
        intent.putExtra("preinfoBean", preinfoBean);
        intent.putExtra("coopera", 1);
        intent.putExtra("did", did);
        startActivity(intent);
        dialog.dismiss();
        getActivity().finish();
    }

    @Override
    public void noData() {
        showNoDataView();
    }


    public void startLoginPage() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cooperationPresenter != null)
            cooperationPresenter.cancelRequest();
    }


}
