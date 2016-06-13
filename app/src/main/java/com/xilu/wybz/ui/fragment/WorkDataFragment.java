package com.xilu.wybz.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.InspireRecordViewHolder;
import com.xilu.wybz.adapter.WorkAdapter;
import com.xilu.wybz.adapter.WorksViewHolder;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.presenter.UserPresenter;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.SpacesItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/6/3.
 */
public class WorkDataFragment extends BaseFragment implements IUserView {
    @Bind(R.id.id_stickynavlayout_innerscrollview)
    ListView listView;
    WorkAdapter adapter;
    UserPresenter userPresenter;
    public static String TYPE = "type";
    public static String UID = "uid";
    public static String AUTHOR = "author";
    private int type;
    private int userId;
    private int deletePos;
    private String COME;
    private String author;
    private List<WorksData> mDataList;
    private boolean isMe;
    private int workType;

    private String[] COMES = new String[]{"myrecord", "mysong", "mylyrics", "myfav"};
    private String[] COMESs = new String[]{"usersong", "userlyrics", "userfav"};

    @Override
    protected int getLayoutResId() {
        return R.layout.base_listview;
    }

    @Override
    protected void initPresenter() {
        userPresenter = new UserPresenter(context, this);
        userPresenter.init();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            type = getArguments().getInt(TYPE);
//            userId = getArguments().getInt(UID);
//            isMe = (userId == PrefsUtil.getUserId(context));
//            if (!isMe) {
//                COME = COMESs[type];
//                type = type + 1;
//            } else {
//                COME = COMES[type];
//                if (type == 0) {
//                    type = 4;
//                    workType = 3;
//                } else {
//                    workType = type;
//                }
//            }
//            author = getArguments().getString(AUTHOR);
//        }

    }

    public static WorkDataFragment newInstance(int type, int userId, String author) {
        WorkDataFragment tabFragment = new WorkDataFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putInt(UID, userId);
        bundle.putString(AUTHOR, author);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    @Override
    public void setUserInfo(UserBean userBean) {
        EventBus.getDefault().post(new Event.UpdataUserBean(userBean, PrefsUtil.getUserId(context) == userId ? 1 : 2));
    }

    @Override
    public void showWorksData(List<WorksData> worksDataList) {

    }


    @Override
    public void loadFail() {

    }

    @Override
    public void loadNoMore() {
    }

    @Override
    public void deleteSuccess() {
        cancelPd();
    }

    @Override
    public void deleteFail() {
        cancelPd();
    }

    @Override
    public void loadNoData() {
    }

    public void deleteWorksData(int pos) {
        showPd("正在删除中...");
        deletePos = pos;
        userPresenter.delete(mDataList.get(pos).getItemid(), workType);
    }

    public void unfavWorksData(int pos) {
        showPd("正在删除中...");
        deletePos = pos;
        WorksData worksData = mDataList.get(pos);
        userPresenter.unfav(worksData.itemid, worksData.uid, worksData.status);
    }

    public void removeData(WorksData worksData) {
//        int index = -1;
//        for(int i=0;i<mDataList.size();i++){
//            if(worksData.itemid==mDataList.get(i).itemid&&worksData.status==mDataList.get(i).status){
//                index = i;
//                break;
//            }
//        }
//        if(index>-1){
//            removeItem(index);
//        }
    }

    @Override
    public void initView() {
        mDataList = new ArrayList<>();
        for(int i=0;i<12;i++){
            mDataList.add(new WorksData());
        }
        adapter = new WorkAdapter(context,mDataList);
        listView.setAdapter(adapter);
    }
}
