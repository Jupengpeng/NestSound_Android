package com.xilu.wybz.ui.preserve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.PersonInfo;
import com.xilu.wybz.bean.ProductInfo;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.ApplyPreservePresenter;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.ui.IView.IApplyPreservView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ApplyPreserveActivity extends ToolbarActivity implements IApplyPreservView {

    public static final String DATA = "data";

    private static String TEXT = "提交申请即表示认同《音巢音乐保全免责申明》";

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
    @Bind(R.id.user_info_title)
    TextView userInfoTitle;
    @Bind(R.id.user_info_right)
    TextView userInfoRight;
    @Bind(R.id.user_info_add)
    TextView userInfoAdd;
    @Bind(R.id.preservation_name)
    TextView preservationName;
    @Bind(R.id.preservation_card_id)
    TextView preservationCardId;
    @Bind(R.id.preservation_phone)
    TextView preservationPhone;
    @Bind(R.id.user_info_container)
    LinearLayout userInfoContainer;
    @Bind(R.id.text_spinner)
    AppCompatSpinner textSpinner;
    @Bind(R.id.price_mask)
    TextView priceMask;
    @Bind(R.id.product_price)
    TextView productPrice;
    @Bind(R.id.wechat_logo)
    ImageView wechatLogo;
    @Bind(R.id.wechat_mask)
    TextView wechatMask;
    @Bind(R.id.wechat_check)
    ImageView wechatCheck;
    @Bind(R.id.preservation_submit)
    TextView preservationCommit;
    @Bind(R.id.preservation_protocol)
    TextView preservationProtocol;

    /**
     *
     */
    @Bind(R.id.iv_nodata)
    ImageView ivNodata;
    @Bind(R.id.tv_nodata)
    TextView tvNodata;
    @Bind(R.id.ll_nodata)
    LinearLayout llNodata;

    /**
     * 请求页面时传过来的数据.
     */
    ProductInfo requestData;
    /**
     *
     */
    ProductInfo productInfo;
    PersonInfo personInfo;

    ApplyPreservePresenter presenter;

    String[] prices = {"2.00", "3.00", "4.00"};

    int selectType = 0;



    @Override
    protected int getLayoutRes() {
        return R.layout.activity_apply_product_preservation;
    }


    /**
     * 打开保全申请页面
     *
     * @param context
     * @param info
     */
    public static void start(Context context, ProductInfo info) {
        Intent intent = new Intent(context, ApplyPreserveActivity.class);
        intent.putExtra(DATA, info);
        context.startActivity(intent);
    }

    /**
     *
     */
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            requestData = intent.getParcelableExtra(DATA);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("保全申请");
        initData();
        initProtocol();

        llNodata.setVisibility(View.VISIBLE);

        userInfoRight.setVisibility(View.GONE);

        initPresenter();

        initProductType(3);


    }

    public void initPresenter() {
        presenter = new ApplyPreservePresenter(context, this);
        presenter.getApplyProdutInfo(requestData.id, requestData.type);
    }

    /**
     * 不使用.
     *
     * @Deprecated.
     */

    @Override
    public void initView() {

    }

    @Override
    public void showError() {
//        llNodata.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPage() {
        llNodata.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void updateProductInfo(ProductInfo info) {
        productInfo = info;
        setProductInfo(info);
    }

    @Override
    public void updateUsePrice(String[] prices, int type) {
        this.prices = prices;
        initProductType(type);
    }

    @Override
    public void updatePersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
        userInfoAdd.setVisibility(View.GONE);
        userInfoContainer.setVisibility(View.VISIBLE);
        setPersonInfo(personInfo);
    }

    /**
     * updateSubmitView.
     *
     * @param type
     */
    @Override
    public void updateSubmitView(int type) {

    }


    /**
     * callback 启动sdk开始支付.
     *
     * @param data
     */
    @Override
    public void startPay(String data) {

        Log.e("pay", "startPay");

        PingppLog.DEBUG = false;
        Pingpp.createPayment(ApplyPreserveActivity.this, data);
    }

    /**
     * 设置作品信息
     *
     * @param info
     */
    private void setProductInfo(ProductInfo info) {

        if (info == null) {
            infoName.setText("作品名：");
            infoLyricAuthor.setText("词作者：");
            infoSongAuthor.setText("曲作者：");
            infoBanzou.setText("伴奏：");
            infoCreateTime.setText("创作时间：");

        } else {

            if (!StringUtils.isBlank(info.image)) {
                loadImage(info.image, productImage);
            } else {

            }

            infoName.setText("作品名：" + info.title);
            /**
             *
             */
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
            /**
             *
             */
            infoCreateTime.setText("词创作时间：" + DateFormatUtils.formatX1(info.updatetime));
        }
    }

    @OnClick(R.id.user_info_add)
    public void onClickAdd() {
        PreservePersonEditActivity.start(this, null);
    }

    @OnClick(R.id.user_info_right)
    public void onClickRight() {

        PreservePersonEditActivity.start(this, personInfo);
    }

    @OnClick(R.id.preservation_submit)
    public void onClickSubmit() {

        ProductInfo productInfo1 = new ProductInfo();

        productInfo1.type = 1;
        productInfo1.id = ""+134245;

        if (productInfo == null){

            productInfo = productInfo1;
        }

//        PersonInfo personInfo1 = new PersonInfo();
//
//        personInfo1.cCardId = "1321415";
//        personInfo1.cUserName = "cUserName";
//        personInfo1.cPhone = "1325354667";

//        if (productInfo == null){
//            ToastUtils.toast(context,"没有作品信息");
//            return;
//        }

        if (personInfo == null) {
            ToastUtils.toast(context, "请添加个人信息");
            return;
        }

        productInfo.cType = selectType;
        productInfo.id = requestData.id;
        productInfo.type = requestData.type;

        presenter.applyOrder(productInfo, personInfo);

//        preservationCommit.setEnabled(false);

    }


    /**
     *
     */
    public void initProtocol() {
        String text = preservationProtocol.getText().toString();
        preservationProtocol.setText(StringStyleUtil.getLinkSpan(this, text, BrowserActivity.class, MyCommon.PROTOCOL_1));
        preservationProtocol.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * initProductType.
     *
     * @param type
     */
    boolean firstSelect = true;

    public void initProductType(int type) {

        switch (type) {

            case 1:
                textSpinner.setSelection(1);
                textSpinner.setEnabled(false);
                selectType = 1;
                break;
            case 2:
                textSpinner.setSelection(2);
                textSpinner.setEnabled(false);
                selectType = 2;
                break;
            default:
                textSpinner.setSelection(0);
                selectType = 3;
                textSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String message = "保全歌曲和歌词";
                        switch (position) {
                            case 0:
                                selectType = 3;
                                message = "保全歌曲和歌词";
                                break;
                            case 1:
                                selectType = 1;
                                message = "仅保全歌曲";
                                break;
                            case 2:
                                selectType = 2;
                                message = "仅保全歌词";
                                break;
                            default:
                                selectType = 3;
                                Log.d("Apply", "textSpinner: select error? default=3");
                        }
                        setNeedPrice(selectType);
                        if (firstSelect) {
                            firstSelect = false;
                            return;
                        }
                        ToastUtils.toast(context, message);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        }
        setNeedPrice(selectType);

    }


    /**
     * setNeedPrice.
     *
     * @param type
     */
    private void setNeedPrice(int type) {
        String unit = "￥";
        productPrice.setText(unit + prices[type - 1]);
    }


    /**
     * @return
     */
    public PersonInfo getPersonInfo() {

        PersonInfo personInfo = new PersonInfo();
        personInfo.cUserName = preservationName.getText().toString();
        personInfo.cCardId = preservationCardId.getText().toString();
        personInfo.cPhone = preservationPhone.getText().toString();
        return personInfo;
    }

    /**
     * @param info
     */
    public void setPersonInfo(PersonInfo info) {

        preservationName.setText(info.cUserName);
        preservationCardId.setText(info.cCardId);
        preservationPhone.setText(info.cPhone);

        userInfoRight.setVisibility(View.VISIBLE);
    }


    /**
     * callback
     *
     * @param status
     */
    protected void payCallback(String status) {

        int state = 0;

        if ("success".equalsIgnoreCase(status)) {
            state = 8;
            ToastUtils.toast(context, "保全作品成功");
            finish();

        } else if ("fail".equalsIgnoreCase(status)) {
            state = 4;
        } else if ("cancel".equalsIgnoreCase(status)) {
            state = 0;
        } else if ("invalid".equalsIgnoreCase(status)) {
            state = 404;
        }

        presenter.applyPayCallback(state);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("pay", "onActivityResult");

        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");

                payCallback(result);

                // 处理返回值
                // "success" - 支付成功
                // "fail"    - 支付失败
                // "cancel"  - 取消支付
                // "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）

                String errorMsg = ":" + data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = ":" + data.getExtras().getString("extra_msg"); // 错误信息

                showMsg(result + errorMsg + extraMsg);

                Log.e("pay", result + ":" + errorMsg + ":" + extraMsg);
            }
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                PersonInfo info = PreservePersonEditActivity.getInfo(data);
                if (info != null) {
                    userInfoAdd.setVisibility(View.GONE);
                    userInfoContainer.setVisibility(View.VISIBLE);

                    personInfo = info;
                    setPersonInfo(info);
                }

            }
//            ToastUtils.toast(this, "onActivityResult:" + requestCode + ":" + resultCode);
        }
    }
}
