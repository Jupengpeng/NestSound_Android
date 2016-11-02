package com.xilu.wybz.ui.cooperation;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperaDetailsBean;
import com.xilu.wybz.bean.CooperationBean;
import com.xilu.wybz.presenter.CooperaDetailsPresenter;
import com.xilu.wybz.ui.IView.ICooperaDetailsView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.view.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/23.
 */

public class CooperaDetailsActivity extends ToolbarActivity implements ICooperaDetailsView {
    int where;// 1  是从合作页面过来的  2 是从我的页面过来的
    @Bind(R.id.layout1)
    RelativeLayout layout1;
    @Bind(R.id.layout2)
    LinearLayout layout2;
    @Bind(R.id.scrollview)
    ScrollView scrollView;
    @Bind(R.id.cooperadetails_tv_name)
    TextView cooperadetails_tv_name;
    @Bind(R.id.cooperadetails_head_iv)
    ImageView cooperadetails_head_iv;
    @Bind(R.id.cooperadetails_tv_time)
    TextView cooperadetails_tv_time;
    @Bind(R.id.cooperadetails_tv_endtime)
    TextView cooperadetails_tv_endtime;
    @Bind(R.id.cooperadetails_tv_requirement)
    TextView cooperadetails_tv_requirement;
    @Bind(R.id.cooperadetails_tv_title)
    TextView cooperadetails_tv_title;
    @Bind(R.id.cooperadetails_tv_lyric)
    TextView cooperadetails_tv_lyric;
    @Bind(R.id.cooperadetails_tv_commentnum)
    TextView cooperadetails_tv_commentnum;
    @Bind(R.id.commentList_recyclerview)
    MyRecyclerView commentList_recyclerview;
    @Bind(R.id.completeList_recyclerview)
    RecyclerView completeList_recyclerview;
    @Bind(R.id.comment_layout)
    LinearLayout comment_layout;
    @Bind(R.id.collect_iv)
    ImageView collect_iv;
    boolean iscollect=false;
    private CooperationBean cooperationBean;
    private CooperaDetailsPresenter cooperaDetailsPresenter;
    private List<CooperaDetailsBean.CommentListBean> CommentList = new ArrayList<>();
    private List<CooperaDetailsBean.CompleteListBean> CompleteList = new ArrayList<>();
    private CommentListAdapter commentListAdapter;
    private CompleteListAdapter completeListAdapter;
    private AlertDialog dialog;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cooperadetails;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("某某的合作");
        initDatas();
        initPresenter();
    }

    private void initPresenter() {
        cooperaDetailsPresenter = new CooperaDetailsPresenter(this, this);
        cooperaDetailsPresenter.init();

    }

    private void initDatas() {
        where = getIntent().getIntExtra("type", 0);
        if (where == 1) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        } else if (where == 2) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        }
    }
    private void initDialog2() {

        AlertDialog dialog = new AlertDialog.Builder(CooperaDetailsActivity.this).create();
        LayoutInflater layoutInflater = LayoutInflater.from(CooperaDetailsActivity.this);
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.hezuotishidialig, null);
        Button positive_bt = (Button) layout.findViewById(R.id.cancle_bt);
        Button cancle_bt = (Button) layout.findViewById(R.id.positive_bt);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        cancle_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        positive_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startLyricDetailsActivity(cooperaLyricBean);//发送post请求将作品至为公开
                dialog.dismiss();
            }
        });

    }

    @OnClick({R.id.layout1, R.id.invitation, R.id.collectcoopera, R.id.coopera, R.id.comment_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout1:
                startActivity(InvitationActivity.class);
                break;
            case R.id.invitation:
                startActivity(InvitationActivity.class);
                break;
            case R.id.collectcoopera:
                iscollect = !iscollect;
                if (iscollect) {
                    //取消收藏
                    collect_iv.setImageResource(R.drawable.ic_shoucangdianji);
                    showMsg("收藏成功!");
                } else {
                    //已收藏
                    collect_iv.setImageResource(R.drawable.ic_shoucang);
                }
                break;
            case R.id.coopera:
                initDialog2();

                break;
            case R.id.comment_layout:
                startActivity(CooperaMessageActivity.class);

                break;
        }
    }

    private void initDialog() {
        dialog = new AlertDialog.Builder(CooperaDetailsActivity.this).create();
        LayoutInflater layoutInflater = LayoutInflater.from(CooperaDetailsActivity.this);
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.caina, null);
        Button positive_bt = (Button) layout.findViewById(R.id.cancle_bt);
        Button cancle_bt = (Button) layout.findViewById(R.id.positive_bt);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        cancle_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        positive_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startLyricDetailsActivity(cooperaLyricBean);//发送post请求将作品至为公开
                dialog.dismiss();
            }
        });

    }

    @Override
    public void initView() {
        commentList_recyclerview.setNestedScrollingEnabled(false);
        completeList_recyclerview.setNestedScrollingEnabled(false);
        cooperaDetailsPresenter.getCooperaDetailsBean();
        commentListAdapter = new CommentListAdapter(CommentList, context);
        commentList_recyclerview.setLayoutManager(new LinearLayoutManager(context));
        commentList_recyclerview.setAdapter(commentListAdapter);
        completeListAdapter = new CompleteListAdapter(CompleteList, context, where);
        completeList_recyclerview.setLayoutManager(new LinearLayoutManager(context));
        completeList_recyclerview.setAdapter(completeListAdapter);
        completeListAdapter.setOnItemClickListener(new CompleteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type) {
                switch (type) {
                    case 1:
                        initDialog();


                        break;

                }
            }
        });

    }

    @Override
    public void showCooperaDetailsBean(CooperaDetailsBean cooperaDetailsBean) {
        cooperadetails_tv_name.setText(cooperaDetailsBean.getUserInfo().getNickname());
        cooperadetails_tv_time.setText(DateFormatUtils.formatX1(cooperaDetailsBean.getDemandInfo().getCreatetime()));
        cooperadetails_tv_endtime.setText("至" + cooperaDetailsBean.getDemandInfo().getEndtime() + "过期");
        cooperadetails_tv_requirement.setText(cooperaDetailsBean.getDemandInfo().getRequirement());
        cooperadetails_tv_title.setText(cooperaDetailsBean.getDemandInfo().getTitle());
        cooperadetails_tv_lyric.setText(cooperaDetailsBean.getDemandInfo().getLyrics());
        cooperadetails_tv_commentnum.setText("全部" + cooperaDetailsBean.getDemandInfo().getCommentnum() + "条留言>>");
        CommentList = cooperaDetailsBean.getCommentList();
        CompleteList = cooperaDetailsBean.getCompleteList();

    }
}
