package com.xilu.wybz.ui.preservation;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.PersonInfo;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.utils.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ApplyPreservActivity extends ToolbarActivity {

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
    Spinner textSpinner;
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

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_apply_product_preservation;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("保全申请");

        initProtocol();
    }

    @OnClick(R.id.user_info_add)
    public void onClickAdd(){
        startActivity(ProductPreservListActivity.class);
    }

    @OnClick(R.id.user_info_right)
    public void onClickRight(){

        PersonInfo personInfo = getPersonInfo();
        PreservPersonEditActivity.startPersonEditActivity(this,personInfo);
    }

    @OnClick(R.id.preservation_submit)
    public void onClickSubmit(){

//        startActivity(PreservInfoActivity.class);
        startActivity(ProductAllActivity.class);

    }


    /**
     *
     */
    public void initProtocol(){
        String text = preservationProtocol.getText().toString();
        preservationProtocol.setText(StringStyleUtil.getLinkSpan(this,text,BrowserActivity.class, MyCommon.PROTOCOL_1));
        preservationProtocol.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     *
     * @return
     */
    public PersonInfo getPersonInfo(){

        PersonInfo personInfo = new PersonInfo();
        personInfo.name = preservationName.getText().toString();
        personInfo.cardID = preservationCardId.getText().toString();
        personInfo.phone = preservationPhone.getText().toString();
        return personInfo;
    }

    /**
     *
     * @param info
     */
    public void setPersonInfo(PersonInfo info){

        preservationName.setText(info.name);
        preservationCardId.setText(info.cardID);
        preservationPhone.setText(info.phone);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK){

                PersonInfo info = PreservPersonEditActivity.getInfo(data);
                if (info != null){
                    userInfoAdd.setVisibility(View.GONE);
                    userInfoContainer.setVisibility(View.VISIBLE);

                    setPersonInfo(info);
                }

            }
            ToastUtils.toast(this,"onActivityResult:"+requestCode+":"+resultCode);

        }
    }
}
