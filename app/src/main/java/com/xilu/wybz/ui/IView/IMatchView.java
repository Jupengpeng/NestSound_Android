package com.xilu.wybz.ui.IView;

import com.xilu.wybz.adapter.JoinUserAdapter;
import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.bean.MatchBean;
import com.xilu.wybz.bean.UserBean;

import java.util.List;

/**
 * Created by June on 16/5/7.
 */
public interface IMatchView extends IBaseView{
    void showMatchData(MatchBean matchBean);
    void showJoinData(List<UserBean> userBeenList);
    void loadOver();
    void loadFail();
}
