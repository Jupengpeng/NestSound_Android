package com.xilu.wybz.ui.preserve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.PersonInfo;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.presenter.SamplePresenter;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.ui.IView.ISampleView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/14.
 */
public class PreservePersonEditActivity extends ToolbarActivity implements ISampleView<PersonInfo> {

    public static String INFO = "INFO";

    @Bind(R.id.preservation_name)
    EditText preservationName;
    @Bind(R.id.preservation_card_id)
    EditText preservationCardId;
    @Bind(R.id.preservation_phone)
    EditText preservationPhone;
    @Bind(R.id.person_info_desc)
    TextView personInfoDesc;
    @Bind(R.id.edit_submit)
    TextView editSubmit;

    PersonInfo personInfo;

    String desc = "《音巢音乐个人信息采集申明》";

    SamplePresenter<PersonInfo> samplePresenter;
    Map<String,String> params;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_preserv_person_edit;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("个人信息填写");

        personInfo = getInfo(getIntent());

        if (personInfo != null) {
            preservationName.setText(personInfo.cUserName);
            preservationCardId.setText(personInfo.cCardId);
            preservationPhone.setText(personInfo.cPhone);
        }

//        initProtocol();
        initPresenter();
    }


    /**
     *
     */
    public void initProtocol(){
        String text = personInfoDesc.getText().toString();
        personInfoDesc.setText(StringStyleUtil.getLinkSpan(this,text,BrowserActivity.class, MyCommon.PROTOCOL_2));
        personInfoDesc.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * initPresenter.
     */
    private void initPresenter(){

        ViewCallback callback = new ViewCallback();

        samplePresenter = new SamplePresenter<>(context,callback);

        samplePresenter.url = MyHttpClient.getSavePersonInfo();
        samplePresenter.resultType = new TypeToken<PersonInfo>(){}.getType();
        params = new HashMap<>();

        params.put("bq_uid",""+ PrefsUtil.getUserId(context));

    }

    @OnClick(R.id.edit_submit)
    public void onClickSubmit() {

        if (!checkNotNull(preservationName)) {
            ToastUtils.toast(this, "请填写保全人姓名");
            return;
        }
        if (!checkNotNull(preservationCardId)) {
            ToastUtils.toast(this, "请填写保全人身份证");
            return;
        }
        if (!checkNotNull(preservationPhone)) {
            ToastUtils.toast(this, "请填写保全人手机号");
            return;
        }

        if (personInfo == null){
            personInfo = new PersonInfo();
        }

        personInfo.cUserName = preservationName.getText().toString();
        personInfo.cCardId = preservationCardId.getText().toString();
        personInfo.cPhone = preservationPhone.getText().toString();


        params.put("bq_username",personInfo.cUserName);
        params.put("bq_phone",personInfo.cPhone);
        params.put("bq_creditID",personInfo.cCardId);

        samplePresenter.getData(params);

        Intent intent = new Intent();
        intent.putExtra(INFO, personInfo);
        setResult(RESULT_OK, intent);
        finish();
    }


    /**
     * @param activity
     * @param info
     */
    public static void start(Activity activity, PersonInfo info) {

        Intent intent = new Intent(activity, PreservePersonEditActivity.class);
        if (info != null) {
            intent.putExtra(INFO, info);
        }
        activity.startActivityForResult(intent, 1);
    }


    @Override
    public void onSuccess(PersonInfo info) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void initView() {

    }

    /**
     * @param intent
     * @return
     */
    public static PersonInfo getInfo(Intent intent) {
        PersonInfo info = intent.getParcelableExtra(INFO);
        return info;
    }


    /**
     * @param view
     * @return
     */
    public boolean checkNotNull(TextView view) {
        if (view == null) {
            return false;
        }
        String text = view.getText().toString();
        if (text == null || (text.trim().length() == 0)) {
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clear, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_clear) {

            preservationName.setText("");
            preservationCardId.setText("");
            preservationPhone.setText("");

            ToastUtils.toast(this, "已经清空信息");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * ViewCallback.
     */
    public static class ViewCallback implements ISampleView<PersonInfo>{

        static String TAG = "ViewCallback";
        @Override
        public void onSuccess(PersonInfo data) {
            Log.d(TAG,"onSuccess");
        }

        @Override
        public void onError(String message) {
            Log.d(TAG,"onError:"+message);
        }

        @Override
        public void initView() {
            Log.d(TAG,"initView");
        }
    }
}
