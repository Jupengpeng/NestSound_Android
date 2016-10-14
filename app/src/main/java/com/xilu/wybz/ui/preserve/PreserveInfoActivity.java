package com.xilu.wybz.ui.preserve;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.PersonInfo;
import com.xilu.wybz.bean.PreserveInfoBean;
import com.xilu.wybz.bean.ProductInfo;
import com.xilu.wybz.bean.VO.NodeViewData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.presenter.SamplePresenter;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.ui.IView.ISampleView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/14.
 */
public class PreserveInfoActivity extends ToolbarActivity implements ISampleView<PreserveInfoBean> {

    public static final String DATA = "data";

    /**
     *
     */
    @Bind(R.id.preservation_name)
    TextView preservationName;
    @Bind(R.id.preservation_card_id)
    TextView preservationCardId;
    @Bind(R.id.preservation_phone)
    TextView preservationPhone;
    @Bind(R.id.preservation_type)
    TextView preservationType;
    @Bind(R.id.product_image)
    ImageView productImage;
    @Bind(R.id.info_name)
    TextView infoName;
    @Bind(R.id.info_lyric_author)
    TextView infoLyricAuthor;
    @Bind(R.id.info_song_author)
    TextView infoSongAuthor;
    @Bind(R.id.info_banzou)
    TextView infoBanzou;
    @Bind(R.id.info_create_time)
    TextView infoCreateTime;
    @Bind(R.id.info_container)
    LinearLayout infoContainer;
    @Bind(R.id.preservation_time)
    TextView preservationTime;
    @Bind(R.id.preservation_number)
    TextView preservationNumber;
    @Bind(R.id.user_info_container)
    LinearLayout userInfoContainer;
    @Bind(R.id.preservation_submit)
    TextView preservationSubmit;
    @Bind(R.id.preservation_error)
    TextView preservationError;
    /**
     * 错误页面.
     */
    @Bind(R.id.iv_nodata)
    ImageView ivNodata;
    @Bind(R.id.tv_nodata)
    TextView tvNodata;
    @Bind(R.id.ll_nodata)
    LinearLayout llNodata;

    SamplePresenter<PreserveInfoBean> samplePresenter;

    NodeViewData nodeViewData;

    String certUrl;

    /**
     * start.
     * @param context
     * @param orderId
     */
    public static void start(Context context, String orderId) {
        NodeViewData data = new NodeViewData();
        data.orderId = orderId;
        startPreservInfoActivity(context, data);
    }

    public static void startPreservInfoActivity(Context context, NodeViewData data) {

        Intent intent = new Intent(context, PreserveInfoActivity.class);
        intent.putExtra(DATA, data);
        context.startActivity(intent);

    }

    public void initInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            nodeViewData = intent.getParcelableExtra(DATA);
        }
    }


    /**
     * setExtInfo.
     *
     * @param bean
     */
    private void setExtInfo(PreserveInfoBean bean) {
        if (bean == null) {
            preservationSubmit.setEnabled(false);
            preservationSubmit.setText("未知状态");
            preservationError.setVisibility(View.GONE);
            return;
        }
        certUrl = new String(bean.certUrl);

        if (bean.statu == 2) {
            preservationSubmit.setEnabled(false);
            preservationSubmit.setText("保全中...");
            preservationError.setVisibility(View.GONE);

        } else if (bean.statu == 3) {
            preservationSubmit.setEnabled(false);
            preservationSubmit.setText("保全失败");
            preservationError.setVisibility(View.VISIBLE);
        } else {
            preservationSubmit.setEnabled(true);
            preservationSubmit.setText("查看保全证书");
            preservationError.setVisibility(View.GONE);
        }

    }

    private void setPersonInfo(PersonInfo info) {
        if (info == null) {
            preservationName.setText("");
            preservationCardId.setText("");
            preservationPhone.setText("");
            preservationType.setText("无");
        } else {
            preservationName.setText(info.cUserName);
            preservationCardId.setText(info.cCardId);
            preservationPhone.setText(info.cPhone);
            preservationType.setText(info.getTypeString());
        }
    }

    private void setProductInfo(ProductInfo info) {

        if (info == null) {
            infoName.setText("歌曲名：");
            infoLyricAuthor.setText("词作者：");
            infoSongAuthor.setText("曲作者：");
            infoBanzou.setText("伴奏：");
            infoCreateTime.setText("创作时间：");
            preservationNumber.setText("保全编号：");
            preservationTime.setText("保全时间：");

        } else {

            if (!StringUtils.isBlank(info.image)) {
                loadImage(info.image, productImage);
            } else {

            }

            infoName.setText("作品名：" + info.title);
            preservationNumber.setText("保全编号：" + info.preserveID);
            preservationTime.setText("保全时间：" + info.preserveDate);

            if (StringUtils.isBlank(info.lyricAuthor)) {
                infoLyricAuthor.setVisibility(View.GONE);
            } else {
                infoLyricAuthor.setVisibility(View.VISIBLE);
                infoLyricAuthor.setText("词作者：" + info.lyricAuthor);
            }
            if (StringUtils.isBlank(info.songAuthor)) {
                infoSongAuthor.setVisibility(View.GONE);
            } else {
                infoSongAuthor.setVisibility(View.VISIBLE);
                infoSongAuthor.setText("曲作者：" + info.songAuthor);
            }
            if (StringUtils.isBlank(info.accompaniment)) {
                infoBanzou.setVisibility(View.GONE);
            } else {
                infoBanzou.setVisibility(View.VISIBLE);
                infoBanzou.setText("伴奏：" + info.accompaniment);
            }
            infoCreateTime.setText("词创作时间：" + DateFormatUtils.formatX1(info.createTime));

//            infoName.setText("歌曲名：" + info.title);
//            infoLyricAuthor.setText("词作者：" + info.lyricAuthor);
//            infoSongAuthor.setText("曲作者：" + info.songAuthor);
//            infoBanzou.setText("伴奏：" + info.accompaniment);
//            infoCreateTime.setText("词创作时间：" + DateFormatUtils.formatX1(info.createTime));
//            preservationNumber.setText("保全编号：" + info.preserveID);
//            preservationTime.setText("保全时间：" + info.preserveDate);
        }
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_preserv_info;
    }


    @OnClick(R.id.preservation_submit)
    public void onclickSubmit() {

        if (StringUtils.isBlank(certUrl)) {
            ToastUtils.toast(context, "证书获取失败！");
            return;
        }

        BrowserActivity.toBrowserActivity(context, certUrl);
    }


    @Override
    public void onSuccess(PreserveInfoBean data) {
        llNodata.setVisibility(View.GONE);
        setPersonInfo(data.personInfo);
        setProductInfo(data.productInfo);
        setExtInfo(data);
        cancelPd();
    }

    @Override
    public void onError(String message) {
        llNodata.setVisibility(View.VISIBLE);
    }

    @Override
    public void initView() {

        setPersonInfo(null);
        setProductInfo(null);
        setExtInfo(null);
        llNodata.setVisibility(View.VISIBLE);
    }


    public void initPresenter() {
        samplePresenter = new SamplePresenter<PreserveInfoBean>(context, this);
        samplePresenter.url = MyHttpClient.getPreserveOrderDetail();
        samplePresenter.resultType = new TypeToken<PreserveInfoBean>(){}.getType();

        Map<String, String> params = new HashMap<>();
        params.put("id", "" + nodeViewData.orderId);
//        params.put("uid", "" + PrefsUtil.getUserId(context));
//        params.put("sort_id", "" + nodeViewData.id);
        samplePresenter.getData(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInfo();
        setTitle("保全单信息");
        initView();
        initPresenter();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_clear, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
