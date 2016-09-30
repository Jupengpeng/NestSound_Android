package com.xilu.wybz.ui.preservation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.VO.NodeViewData;
import com.xilu.wybz.ui.base.ToolbarActivity;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/9/14.
 */
public class PreservInfoActivity extends ToolbarActivity {

    public static final String DATA = "data";

    NodeViewData nodeViewData;
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




    public static void start(Context context, int orderId) {
        NodeViewData data = new NodeViewData();
        data.id = orderId;
        startPreservInfoActivity(context, data);
    }

    public static void startPreservInfoActivity(Context context, NodeViewData data) {

        Intent intent = new Intent(context, PreservInfoActivity.class);
        intent.putExtra(DATA, data);
        context.startActivity(intent);

    }

    public void initInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            nodeViewData = intent.getParcelableExtra(DATA);
        }
    }




    @Override
    protected int getLayoutRes() {
        return R.layout.activity_preserv_info;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInfo();
        setTitle("保全单信息");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clear, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
