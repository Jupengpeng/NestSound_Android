package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.PersonInfo;
import com.xilu.wybz.bean.ProductInfo;

/**
 * Created by Administrator on 2016/9/29.
 */

public interface IApplyPreservView extends IBaseView{

    void updateProductInfo(ProductInfo info);
    void updateUsePrice( String price);

    void updatePersonInfo( PersonInfo personInfo);
    void updateSubmitView( int type);


    void startPay(String data);

}
