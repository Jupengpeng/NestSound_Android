package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.JoinUserBean;
import com.xilu.wybz.bean.MatchBean;

import java.util.List;

/**
 * Created by June on 16/5/7.
 */
public interface IMatchView extends IBaseView{
    void showMatchData(MatchBean matchBean);
    void showJoinData(List<JoinUserBean> joinUserBeenList);
    void loadOver();
    void loadFail();
}
