package com.xilu.wybz.ui.cooperation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperaDetailsBean;
import com.xilu.wybz.bean.PreinfoBean;
import com.xilu.wybz.presenter.CooperaDetailsPresenter;
import com.xilu.wybz.ui.IView.ICooperaDetailsView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.mine.OtherUserCenterActivity;
import com.xilu.wybz.ui.mine.UserCenterActivity;
import com.xilu.wybz.ui.song.HotCatalogActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.xilu.wybz.R.id.cooperadetails_head_iv;
import static com.xilu.wybz.R.id.positive_bt;

public class CooperaDetailesActivity extends ToolbarActivity implements ICooperaDetailsView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.listview)
    ListView lv;
    @Bind(R.id.ll_loading)
    LinearLayout llloading;
    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @Bind(R.id.layout1)
    RelativeLayout layout1;
    @Bind(R.id.layout2)
    LinearLayout layout2;
    @Bind(R.id.collect_iv)
    ImageView collect_iv;
    private int visiableLastIndex = 0;
    private int visiableItemCounts = 0;
    private boolean isfootView = false;
    int iscollect;
    private CooperaDetailsBean detailsBean;
    ImageView nocoopera;
    private List<CooperaDetailsBean.CompleteListBean> completeList = new ArrayList<>();
    private List<CooperaDetailsBean.CompleteListBean> totalcompleteList = new ArrayList<>();
    private CooperaDetailsPresenter cooperaDetailsPresenter;
    int where;// 1  是从合作页面过来的  2 是从我的页面过来的
    private AlertDialog dialog;
    private AlertDialog dialog2;
    private int id; //合作ID
    private int page = 1;
    private int itemid;
    private boolean isHasData = true;

    private List<CooperaDetailsBean.CommentListBean> CommentList = new ArrayList<>();
    private CommentListAdapter commentListAdapter;
    private CooperaDetatilesAdapter completeAdapter;
    private CircleImageView circlehead;
    private TextView cooperadetails_tv_name;
    private TextView cooperadetails_tv_time;
    private TextView cooperadetails_tv_endtime;
    private TextView cooperadetails_tv_requirement;
    private TextView cooperadetails_tv_title;
    private TextView cooperadetails_tv_nickname;
    private TextView cooperadetails_tv_commentnum;
    private TextView cooperadetails_tv_lyric;
    private RecyclerView commentList_recyclerview;
    private LinearLayout comment_layout;
    View foot;
    int pushtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("合作");
        initData();
        initPresenter();
    }

    public static void start(Context context, int did, int pushtype, int type) {
        Intent intent = new Intent(context, CooperaDetailesActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("pushtype", pushtype);
        intent.putExtra("did", did);
        context.startActivity(intent);

    }

    private void initData() {
        where = getIntent().getIntExtra("type", 0);
        id = getIntent().getIntExtra("did", 0);
        pushtype = getIntent().getIntExtra("pushtype", 0);
        if (where == 1) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);

        } else if (where == 2) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        }
        if (pushtype == 3) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
        }
    }

    private void initPresenter() {

        cooperaDetailsPresenter = new CooperaDetailsPresenter(this, this);
        showLoading(llloading);
        refreshLayout.setVisibility(View.GONE);
        cooperaDetailsPresenter.init();

    }

    @Override
    public void initView() {
        View v = View.inflate(context, R.layout.cooperadetaileshead, null);
        foot = View.inflate(context, R.layout.widget_pull_to_refresh_footer, null);
        lv.addHeaderView(v);
        nocoopera = ((ImageView) v.findViewById(R.id.ic_coopera));
        circlehead = ((CircleImageView) v.findViewById(cooperadetails_head_iv));
        cooperadetails_tv_name = ((TextView) v.findViewById(R.id.cooperadetails_tv_name));
        cooperadetails_tv_time = ((TextView) v.findViewById(R.id.cooperadetails_tv_time));
        cooperadetails_tv_endtime = ((TextView) v.findViewById(R.id.cooperadetails_tv_endtime));
        cooperadetails_tv_requirement = ((TextView) v.findViewById(R.id.cooperadetails_tv_requirement));
        cooperadetails_tv_title = ((TextView) v.findViewById(R.id.cooperadetails_tv_title));
        cooperadetails_tv_nickname = ((TextView) v.findViewById(R.id.cooperadetails_tv_nickname));
        cooperadetails_tv_lyric = ((TextView) v.findViewById(R.id.cooperadetails_tv_lyric));
        cooperadetails_tv_commentnum = ((TextView) v.findViewById(R.id.cooperadetails_tv_commentnum));
        commentList_recyclerview = ((RecyclerView) v.findViewById(R.id.commentList_recyclerview));
        comment_layout = ((LinearLayout) v.findViewById(R.id.comment_layout));
        completeAdapter = new CooperaDetatilesAdapter(completeList, this, where);
        lv.setAdapter(completeAdapter);
        commentList_recyclerview.setNestedScrollingEnabled(false);
        commentList_recyclerview.setLayoutManager(new LinearLayoutManager(context));
        cooperaDetailsPresenter.getCooperaDetailsBean(id, page);
        refreshLayout.setOnRefreshListener(this);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int itemLastIndex = completeAdapter.getCount() - 1;
                int lastIndex = itemLastIndex + 1;
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visiableLastIndex == lastIndex && isHasData == true) {
                    lv.addFooterView(foot);
                    page++;
                    cooperaDetailsPresenter.getCooperaDetailsBean(id, page);
                    isfootView = true;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visiableItemCounts = visibleItemCount;
                visiableLastIndex = firstVisibleItem + visiableItemCounts - 1;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayAudioActivity.toPlayAudioActivity(context, completeList.get(position - 1).getItemid() + "", "", "hezuo");
            }
        });

        comment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CooperaDetailesActivity.this, CooperaMessageActivity.class);
                intent1.putExtra("did", detailsBean.getDemandInfo().getId());
                intent1.putExtra("commentnum", detailsBean.getDemandInfo().getCommentnum());
                intent1.putExtra("itemid", detailsBean.getDemandInfo().getItemid());

                startActivityForResult(intent1, 100);

            }
        });
        circlehead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int uid = detailsBean.getUserInfo().getUid();
                if (uid != PrefsUtil.getUserId(context)) {
                    OtherUserCenterActivity.toUserInfoActivity(context, uid, detailsBean.getUserInfo().getNickname());
                } else {
                    startActivity(UserCenterActivity.class);
                }
            }
        });
    }

    @OnClick({R.id.layout1, R.id.invitation, R.id.collectcoopera, R.id.coopera})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout1:
                Intent intent2 = new Intent(CooperaDetailesActivity.this, InvitationActivity.class);
                intent2.putExtra("did", detailsBean.getDemandInfo().getId());
                startActivity(intent2);
                break;
            case R.id.invitation:
                Intent intent = new Intent(CooperaDetailesActivity.this, InvitationActivity.class);
                intent.putExtra("did", detailsBean.getDemandInfo().getId());
                startActivity(intent);
                break;
            case R.id.collectcoopera:
                if (iscollect == 0) {
                    //取消收藏
                    iscollect = 1;
                    collect_iv.setImageResource(R.drawable.ic_shoucangdianji);
                    cooperaDetailsPresenter.collect(id, 1);
                } else if (iscollect == 1) {
                    //已收藏
                    iscollect = 0;
                    cooperaDetailsPresenter.collect(id, 0);
                    collect_iv.setImageResource(R.drawable.ic_shoucang);
                }
                break;
            case R.id.coopera:
                initDialog2();
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_coopera_detailes;
    }

    @Override
    public void showCooperaDetailsBean(CooperaDetailsBean cooperaDetailsBean) {
        disMissLoading(llloading);
        if (refreshLayout != null) {
            refreshLayout.setVisibility(View.VISIBLE);
        }
        detailsBean = cooperaDetailsBean;
        setTitle(cooperaDetailsBean.getUserInfo().getNickname() + "的合作");
        cooperadetails_tv_nickname.setText("作词:" + cooperaDetailsBean.getUserInfo().getNickname());
        cooperadetails_tv_name.setText(cooperaDetailsBean.getUserInfo().getNickname());
        cooperadetails_tv_time.setText(DateFormatUtils.formatX1(cooperaDetailsBean.getDemandInfo().getCreatetime()));
        cooperadetails_tv_endtime.setText("至" + DateFormatUtils.formatX1(cooperaDetailsBean.getDemandInfo().getEndtime()).substring(5, 10) + "过期");
        cooperadetails_tv_requirement.setText(cooperaDetailsBean.getDemandInfo().getRequirement());
        cooperadetails_tv_title.setText(cooperaDetailsBean.getDemandInfo().getTitle());
        cooperadetails_tv_lyric.setText(cooperaDetailsBean.getDemandInfo().getLyrics());
        cooperadetails_tv_commentnum.setText("全部" + cooperaDetailsBean.getDemandInfo().getCommentnum() + "条留言>>");
        loadImage(cooperaDetailsBean.getUserInfo().getHeadurl(), circlehead);
        iscollect = cooperaDetailsBean.getDemandInfo().getIscollect();
        if (iscollect == 1) {
            collect_iv.setImageResource(R.drawable.ic_shoucangdianji);
        }
    }


    private void initDialog(int pos) {
        dialog = new AlertDialog.Builder(CooperaDetailesActivity.this).create();
        LayoutInflater layoutInflater = LayoutInflater.from(CooperaDetailesActivity.this);
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.caina, null);
        Button cancle_bt = (Button) layout.findViewById(R.id.cancle_bt);
        Button positive_bto = (Button) layout.findViewById(positive_bt);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        cancle_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        positive_bto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cooperaDetailsPresenter.accept(id, itemid, pos);
            }
        });

    }

    private void initDialog2() {

        dialog2 = new AlertDialog.Builder(CooperaDetailesActivity.this).create();
        LayoutInflater layoutInflater = LayoutInflater.from(CooperaDetailesActivity.this);
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.hezuotishidialig, null);
        Button positive_bts = (Button) layout.findViewById(R.id.cancle_bt);
        Button cancle_bt = (Button) layout.findViewById(positive_bt);
        dialog2.show();
        dialog2.getWindow().setContentView(layout);
        cancle_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cooperaDetailsPresenter.getPreinfo(id);
            }
        });
        positive_bts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startLyricDetailsActivity(cooperaLyricBean);//发送post请求将作品至为公开
                dialog2.dismiss();
            }
        });

    }

    @Override
    public void showCooperaCommentList(List<CooperaDetailsBean.CommentListBean> commentListBean) {

        commentListAdapter = new CommentListAdapter(commentListBean, this);
        commentList_recyclerview.setAdapter(commentListAdapter);
        commentListAdapter.notifyDataSetChanged();
        commentListAdapter.setOnItemClickListener(new CommentListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int uid = commentListBean.get(position).getUid();
                if (uid != PrefsUtil.getUserId(context)) {
                    OtherUserCenterActivity.toUserInfoActivity(context, uid, commentListBean.get(position).getNickname());
                } else {
                    startActivity(UserCenterActivity.class);
                }
            }
        });

    }

    @Override
    public void showCooperaCompleteList(List<CooperaDetailsBean.CompleteListBean> completeListBeen) {
        if (isfootView == true) {
            lv.removeFooterView(foot);
            isfootView = false;
        }
        completeList.addAll(completeListBeen);
        completeAdapter.notifyDataSetChanged();
        completeAdapter.setOnItemClickListener(new CompleteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type) {
                itemid = completeListBeen.get(position).getItemid();
                initDialog(position);
            }
        });
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void collectSuccess(int type) {
        if (type == 1) {
            showMsg("收藏成功!");
        } else if (type == 0) {
            showMsg("取消收藏!");
        }
    }

    @Override
    public void showpreinfoBean(PreinfoBean preinfoBean) {
        Intent intent = new Intent(CooperaDetailesActivity.this, HotCatalogActivity.class);
        intent.putExtra("preinfoBean", preinfoBean);
        intent.putExtra("coopera", 1);
        intent.putExtra("did", id);
        startActivity(intent);
        dialog2.dismiss();
        finish();
    }

    @Override
    public void acceptSuccess(int pos) {
        dialog.dismiss();
        onRefresh();
    }

    @Override
    public void noCompleteData() {
        nocoopera.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMoreCompleteData() {
        isHasData = false;
    }


    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                completeList.clear();
                page = 1;
                cooperaDetailsPresenter.getCooperaDetailsBean(id, page);
                isHasData = true;
            }
        }, 2000);
    }
}
