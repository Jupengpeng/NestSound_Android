package com.xilu.wybz.ui.record;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.PhotoAdapter;
import com.xilu.wybz.bean.PhotoBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.menuitem.SelectPicProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/5/19.
 */
public class SelectPicActivity extends ToolbarActivity {
    @Bind(R.id.recycler)
    RecyclerView recycler;
    List<PhotoBean> mList;
    PhotoAdapter photoAdapter;
    SelectPicProvider selectPicProvider;
    ArrayList<String> picList;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_selectpic_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView() {
        setTitle("所有照片");
        picList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            picList = bundle.getStringArrayList("pics");
        }
        int space10 = DensityUtil.dip2px(context, 10);
        recycler.setLayoutManager(new GridLayoutManager(context, 4));
        recycler.addItemDecoration(new GridSpacingItemDecoration(4, space10, false));
        getPics();
    }
    // 遍历接收一个文件路径，然后把文件子目录中的所有文件遍历并输出来
    private void getPics(){
        mList = new ArrayList<>();
        Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
        MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);
        cursor.moveToLast();
        while (!cursor.isBeforeFirst()) {
            String filepath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            File file = new File(filepath);
            PhotoBean photoBean = new PhotoBean();
            photoBean.path=file.getAbsolutePath();
            if(picList.size()>0)//恢复选中的状态
            photoBean.isCheched = picList.contains(photoBean.path);
            mList.add(photoBean);
            cursor.moveToPrevious();
        }
        cursor.close();
        photoAdapter = new PhotoAdapter(context,mList);
        recycler.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(!mList.get(position).isCheched){
                    if(picList.size()==9){
                        showMsg("最多只能选择9张图片");
                        return;
                    }
                }
                mList.get(position).isCheched = !mList.get(position).isCheched;
                if(mList.get(position).isCheched){
                    picList.add(mList.get(position).path);
                }else if(picList.contains(mList.get(position).path)){
                    picList.remove(mList.get(position).path);//移除
                }
                if(selectPicProvider!=null){
                    selectPicProvider.setCount(picList.size());
                }
                photoAdapter.notifyDataSetChanged();
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_pic, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_select);
        selectPicProvider = (SelectPicProvider)MenuItemCompat.getActionProvider(menuItem);;
        if(picList.size()>0){
            selectPicProvider.setCount(picList.size());
        }
        selectPicProvider.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(picList.size()>0){
                    List<PhotoBean> photoBeens = new ArrayList<>();
                    for(String str:picList){
                        PhotoBean photoBean = new PhotoBean();
                        photoBean.path = str;
                        photoBeens.add(photoBean);
                    }
                    EventBus.getDefault().post(new Event.SelectPicEvent(photoBeens));
                    finish();
                }
            }
        });
        return true;
    }
}
