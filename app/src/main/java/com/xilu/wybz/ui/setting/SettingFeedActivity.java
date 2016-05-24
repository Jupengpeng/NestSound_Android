package com.xilu.wybz.ui.setting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import com.xilu.wybz.R;
import com.xilu.wybz.presenter.FeedbackPresenter;
import com.xilu.wybz.ui.IView.IFeedbackView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import butterknife.Bind;

/**
 * Modify by June on 2016/3/25.
 * 意见反馈
 */
public class SettingFeedActivity extends ToolbarActivity implements IFeedbackView{
    @Bind(R.id.tv_number)
    TextView tv_number;
    @Bind(R.id.et_content)
    EditText et_content;
    @Bind(R.id.et_contact_info)
    EditText et_contactInfo;
    final int charMaxNum = 200;
    int type;
    FeedbackPresenter feedbackPresenter;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_setting_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedbackPresenter = new FeedbackPresenter(this,this);
    }

    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt("type");
            if (type == 1) {
                setTitle("举报");
                et_content.setHint("请输入您的举报内容");
            }
        }
        setTitle("意见反馈");
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_number.setText("还可以输入" + (charMaxNum - s.length()) + "字");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send,menu);
        MenuItem menuItem = menu.findItem(0);
        menuItem.setTitle("提交");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_send:
                toPass();
                break;
        }
        return true;
    }

    private void toPass() {
        String content = et_content.getText().toString();
        String us = et_contactInfo.getText().toString();
        if (content == null || content.trim().equals("")) {
            showMsg("内容不能为空");
            return;
        }
        if (us == null || us.trim().equals("")) {
            showMsg("手机号或email不能为空");
            return;
        }
        feedbackPresenter.postData(userId,us,content,type);
    }

    @Override
    public void postSuccess() {

    }

    @Override
    public void postFail() {

    }
}
