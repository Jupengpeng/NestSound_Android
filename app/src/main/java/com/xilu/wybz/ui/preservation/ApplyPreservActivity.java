package com.xilu.wybz.ui.preservation;

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
import com.xilu.wybz.presenter.ApplyPreservProsenter;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.ui.IView.IApplyPreservView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.utils.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ApplyPreservActivity extends ToolbarActivity implements IApplyPreservView {

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


    ProductInfo productInfo;

    ApplyPreservProsenter prosenter;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_apply_product_preservation;
    }


    public static void startApplyPreservActivity(Context context, ProductInfo info) {
        Intent intent = new Intent(context, ApplyPreservActivity.class);
        intent.putExtra(DATA, info);
        context.startActivity(intent);
    }

    /**
     *
     */
    public void initProductInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            productInfo = intent.getParcelableExtra(DATA);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProductInfo();
        initProtocol();
        setTitle("保全申请");

        initView();
    }


    @Override
    public void initView() {

        prosenter = new ApplyPreservProsenter(context, this);

        initUserType();

    }

    @Override
    public void updateProductInfo(ProductInfo info) {

    }

    @Override
    public void updateUsePrice(String price) {

    }

    @Override
    public void updatePersonInfo(PersonInfo personInfo) {

    }

    @Override
    public void updateSubmitView(int type) {

    }


    @Override
    public void startPay(String data) {

        Log.e("pay","startPay");
//        Log.e("pay","data:"+data);

        PingppLog.DEBUG = false;
//
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
                Pingpp.createPayment(ApplyPreservActivity.this, data);
//            }
//        });




    }

    @OnClick(R.id.user_info_add)
    public void onClickAdd() {

//        PreservPersonEditActivity.startPersonEditActivity(this, null);
    }

    @OnClick(R.id.user_info_right)
    public void onClickRight() {

        PersonInfo personInfo = getPersonInfo();
        PreservPersonEditActivity.startPersonEditActivity(this, personInfo);
    }

    @OnClick(R.id.preservation_submit)
    public void onClickSubmit() {

        ProductInfo productInfo1 = new ProductInfo();

        productInfo1.typeId ="1";
        productInfo1.id="134245";
        PersonInfo personInfo1 = new PersonInfo();
        personInfo1.cardID="1321415";
        personInfo1.name="name";
        personInfo1.phone="1325354667";

        prosenter.applyOrder(productInfo1 , personInfo1);

//        startActivity(PreservInfoActivity.class);
//        startActivity(ProductAllActivity.class);

    }


    /**
     *
     */
    public void initProtocol() {
        String text = preservationProtocol.getText().toString();
        preservationProtocol.setText(StringStyleUtil.getLinkSpan(this, text, BrowserActivity.class, MyCommon.PROTOCOL_1));
        preservationProtocol.setMovementMethod(LinkMovementMethod.getInstance());
    }


    public void initUserType() {

        textSpinner.setSelection(1);
        textSpinner.setEnabled(false);
        textSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("XXX","xxx"+position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * @return
     */
    public PersonInfo getPersonInfo() {

        PersonInfo personInfo = new PersonInfo();
        personInfo.name = preservationName.getText().toString();
        personInfo.cardID = preservationCardId.getText().toString();
        personInfo.phone = preservationPhone.getText().toString();
        return personInfo;
    }

    /**
     * @param info
     */
    public void setPersonInfo(PersonInfo info) {

        preservationName.setText(info.name);
        preservationCardId.setText(info.cardID);
        preservationPhone.setText(info.phone);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("pay","onActivityResult");

        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");

                // 处理返回值
                // "success" - 支付成功
                // "fail"    - 支付失败
                // "cancel"  - 取消支付
                // "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）

                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                showMsg(result+errorMsg+extraMsg);

                Log.e("pay",result+errorMsg+extraMsg);
            }
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                PersonInfo info = PreservPersonEditActivity.getInfo(data);
                if (info != null) {
                    userInfoAdd.setVisibility(View.GONE);
                    userInfoContainer.setVisibility(View.VISIBLE);

                    setPersonInfo(info);
                }

            }
            ToastUtils.toast(this, "onActivityResult:" + requestCode + ":" + resultCode);
        }
    }
}
