package com.xilu.wybz.ui.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperaLyricBean;
import com.xilu.wybz.presenter.CooperaPublishPresenter;
import com.xilu.wybz.ui.IView.ICooperaPublishView;
import com.xilu.wybz.ui.base.ToolbarActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class CooperaPublish extends ToolbarActivity implements ICooperaPublishView {
    CooperaPublishPresenter cooperaPublishPresenter;
    CooperaLyricBean cooperaLyricBean;
    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.editText)
    EditText editText;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_coopera_publish;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();

    }

    private void initPresenter() {
        cooperaPublishPresenter = new CooperaPublishPresenter(context, this);
        cooperaPublishPresenter.init();
    }

    @OnClick({R.id.chooselyric_iv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooselyric_iv:
                Intent intent = new Intent(context, ChooseLyricActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_publish) {//发送post请求 携带参数itemid 和 要求

            if (cooperaLyricBean == null || "".equals(editText.getText().toString().trim())) {
                showMsg("请选择歌曲");
            } else {
                cooperaPublishPresenter.publishDemand(editText.getText().toString().trim(), cooperaLyricBean.getItemid());

            }


            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        cooperaLyricBean = (CooperaLyricBean) intent.getSerializableExtra("cooperaLyricBean");
        if (cooperaLyricBean != null) {
            title_tv.setText(cooperaLyricBean.getTitle());
        }
    }

    @Override
    public void success() {
        showMsg("发布成功");
        Intent intent = new Intent(CooperaPublish.this, CooperationFragment.class);
        intent.putExtra("success", "success");
        startActivity(intent);
    }

    @Override
    public void fail() {
        showMsg("网络出小差了~~~");
    }

    @Override
    public void initView() {


    }
}
